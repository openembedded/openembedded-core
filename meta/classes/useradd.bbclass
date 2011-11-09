# base-passwd-cross provides the default passwd and group files in the
# target sysroot, and shadow -native and -sysroot provide the utilities
# and support files needed to add and modify user and group accounts
DEPENDS_append = "${USERADDDEPENDS}"
USERADDDEPENDS = " base-passwd shadow-native shadow-sysroot"
USERADDDEPENDS_virtclass-nativesdk = ""

# This preinstall function will be run in two contexts: once for the
# native sysroot (as invoked by the useradd_sysroot() wrapper), and
# also as the preinst script in the target package.
useradd_preinst () {
OPT=""
SYSROOT=""

if test "x$D" != "x"; then
	# Installing into a sysroot
	SYSROOT="$D"
	OPT="--root $D"

	# Add groups and users defined for all recipe packages
	GROUPADD_PARAM="${@get_all_cmd_params(d, 'group')}"
	USERADD_PARAM="${@get_all_cmd_params(d, 'user')}"
else
	# Installing onto a target
	# Add groups and users defined only for this package
	GROUPADD_PARAM="${GROUPADD_PARAM}"
	USERADD_PARAM="${USERADD_PARAM}"
fi

# Perform group additions first, since user additions may depend
# on these groups existing
if test "x$GROUPADD_PARAM" != "x"; then
	echo "Running groupadd commands..."
	# Invoke multiple instances of groupadd for parameter lists
	# separated by ';'
	opts=`echo "$GROUPADD_PARAM" | cut -d ';' -f 1`
	remaining=`echo "$GROUPADD_PARAM" | cut -d ';' -f 2-`
	while test "x$opts" != "x"; do
		groupname=`echo "$opts" | awk '{ print $NF }'`
		group_exists=`grep "^$groupname:" $SYSROOT/etc/group || true`
		if test "x$group_exists" = "x"; then
			eval $PSEUDO groupadd  $OPT $opts
		else
			echo "Note: group $groupname already exists, not re-creating it"
		fi

		if test "x$opts" = "x$remaining"; then
			break
		fi
		opts=`echo "$remaining" | cut -d ';' -f 1`
		remaining=`echo "$remaining" | cut -d ';' -f 2-`
	done
fi 

if test "x$USERADD_PARAM" != "x"; then
	echo "Running useradd commands..."
	# Invoke multiple instances of useradd for parameter lists
	# separated by ';'
	opts=`echo "$USERADD_PARAM" | cut -d ';' -f 1`
	remaining=`echo "$USERADD_PARAM" | cut -d ';' -f 2-`
	while test "x$opts" != "x"; do
		# useradd does not have a -f option, so we have to check if the
		# username already exists manually
		username=`echo "$opts" | awk '{ print $NF }'`
		user_exists=`grep "^$username:" $SYSROOT/etc/passwd || true`
		if test "x$user_exists" = "x"; then
			eval $PSEUDO useradd $OPT $opts
		else
			echo "Note: username $username already exists, not re-creating it"
		fi

		if test "x$opts" = "x$remaining"; then
			break
		fi
		opts=`echo "$remaining" | cut -d ';' -f 1`
		remaining=`echo "$remaining" | cut -d ';' -f 2-`
	done
fi
}

useradd_sysroot () {
	export PSEUDO="${STAGING_DIR_NATIVE}${bindir}/pseudo"
	export PSEUDO_LOCALSTATEDIR="${STAGING_DIR_TARGET}${localstatedir}/pseudo"

	# Explicitly set $D since it isn't set to anything
	# before do_install
	D=${STAGING_DIR_TARGET}
	useradd_preinst
}

useradd_sysroot_sstate () {
	if [ "${BB_CURRENTTASK}" = "populate_sysroot_setscene" ]
	then
		useradd_sysroot
	fi
}

do_install[prefuncs] += "${SYSROOTFUNC}"
SYSROOTFUNC = "useradd_sysroot"
SYSROOTFUNC_virtclass-nativesdk = ""
SSTATEPOSTINSTFUNCS += "${SYSROOTPOSTFUNC}"
SYSROOTPOSTFUNC = "useradd_sysroot_sstate"
SYSROOTPOSTFUNC_virtclass-nativesdk = ""

# Recipe parse-time sanity checks
def update_useradd_after_parse(d):
	useradd_packages = d.getVar('USERADD_PACKAGES', True)

	if not useradd_packages:
		raise bb.build.FuncFailed, "%s inherits useradd but doesn't set USERADD_PACKAGES" % bb.data.getVar('FILE', d)

	for pkg in useradd_packages.split():
		if not d.getVar('USERADD_PARAM_%s' % pkg, True) and not d.getVar('GROUPADD_PARAM_%s' % pkg, True):
			raise bb.build.FuncFailed, "%s inherits useradd but doesn't set USERADD_PARAM or GROUPADD_PARAM for package %s" % (bb.data.getVar('FILE', d), pkg)

python __anonymous() {
	update_useradd_after_parse(d)
}

# Return a single [GROUP|USER]ADD_PARAM formatted string which includes the
# [group|user]add parameters for all USERADD_PACKAGES in this recipe
def get_all_cmd_params(d, cmd_type):
	import string
	
	param_type = cmd_type.upper() + "ADD_PARAM_%s"
	params = []

	useradd_packages = d.getVar('USERADD_PACKAGES', True) or ""
	for pkg in useradd_packages.split():
		param = d.getVar(param_type % pkg, True)
		if param:
			params.append(param)

	return string.join(params, "; ")

# Adds the preinst script into generated packages
fakeroot python populate_packages_prepend () {
	def update_useradd_package(pkg):
		bb.debug(1, 'adding user/group calls to preinst for %s' % pkg)

		"""
		useradd preinst is appended here because pkg_preinst may be
		required to execute on the target. Not doing so may cause
		useradd preinst to be invoked twice, causing unwanted warnings.
		"""
		preinst = d.getVar('pkg_preinst_%s' % pkg, True) or d.getVar('pkg_preinst', True)
		if not preinst:
			preinst = '#!/bin/sh\n'
		preinst += d.getVar('useradd_preinst', True)
		bb.data.setVar('pkg_preinst_%s' % pkg, preinst, d)

		# RDEPENDS setup
		rdepends = d.getVar("RDEPENDS_%s" % pkg, True) or ""
		rdepends += " base-passwd shadow"
		bb.data.setVar("RDEPENDS_%s" % pkg, rdepends, d)
	
	# Add the user/group preinstall scripts and RDEPENDS requirements
	# to packages specified by USERADD_PACKAGES
	useradd_packages = d.getVar('USERADD_PACKAGES', True) or ""
	for pkg in useradd_packages.split():
		update_useradd_package(pkg)
}
