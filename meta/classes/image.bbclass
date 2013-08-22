inherit rootfs_${IMAGE_PKGTYPE}

inherit populate_sdk_base

TOOLCHAIN_TARGET_TASK += "${PACKAGE_INSTALL}"
TOOLCHAIN_TARGET_TASK_ATTEMPTONLY += "${PACKAGE_INSTALL_ATTEMPTONLY}"
POPULATE_SDK_POST_TARGET_COMMAND += "rootfs_install_complementary populate_sdk; "

inherit gzipnative

LICENSE = "MIT"
PACKAGES = ""
DEPENDS += "${MLPREFIX}qemuwrapper-cross ${MLPREFIX}depmodwrapper-cross"
RDEPENDS += "${PACKAGE_INSTALL} ${LINGUAS_INSTALL}"
RRECOMMENDS += "${PACKAGE_INSTALL_ATTEMPTONLY}"

INHIBIT_DEFAULT_DEPS = "1"

# IMAGE_FEATURES may contain any available package group
IMAGE_FEATURES ?= ""
IMAGE_FEATURES[type] = "list"
IMAGE_FEATURES[validitems] += "debug-tweaks read-only-rootfs package-management"

# rootfs bootstrap install
ROOTFS_BOOTSTRAP_INSTALL = "${@base_contains("IMAGE_FEATURES", "package-management", "", "${ROOTFS_PKGMANAGE_BOOTSTRAP}",d)}"

# packages to install from features
FEATURE_INSTALL = "${@' '.join(oe.packagegroup.required_packages(oe.data.typed_value('IMAGE_FEATURES', d), d))}"
FEATURE_INSTALL_OPTIONAL = "${@' '.join(oe.packagegroup.optional_packages(oe.data.typed_value('IMAGE_FEATURES', d), d))}"

# Define some very basic feature package groups
SPLASH ?= "psplash"
PACKAGE_GROUP_splash = "${SPLASH}"

# Wildcards specifying complementary packages to install for every package that has been explicitly
# installed into the rootfs
COMPLEMENTARY_GLOB[dev-pkgs] = '*-dev'
COMPLEMENTARY_GLOB[staticdev-pkgs] = '*-staticdev'
COMPLEMENTARY_GLOB[doc-pkgs] = '*-doc'
COMPLEMENTARY_GLOB[dbg-pkgs] = '*-dbg'
COMPLEMENTARY_GLOB[ptest-pkgs] = '*-ptest'

def complementary_globs(featurevar, d):
    all_globs = d.getVarFlags('COMPLEMENTARY_GLOB')
    globs = []
    features = set((d.getVar(featurevar, True) or '').split())
    for name, glob in all_globs.items():
        if name in features:
            globs.append(glob)
    return ' '.join(globs)

IMAGE_INSTALL_COMPLEMENTARY = '${@complementary_globs("IMAGE_FEATURES", d)}'
SDKIMAGE_FEATURES ??= "dev-pkgs dbg-pkgs"
SDKIMAGE_INSTALL_COMPLEMENTARY = '${@complementary_globs("SDKIMAGE_FEATURES", d)}'

def check_image_features(d):
    valid_features = (d.getVarFlag('IMAGE_FEATURES', 'validitems', True) or "").split()
    valid_features += d.getVarFlags('COMPLEMENTARY_GLOB').keys()
    for var in d:
       if var.startswith("PACKAGE_GROUP_"):
           valid_features.append(var[14:])
    valid_features.sort()

    features = set(oe.data.typed_value('IMAGE_FEATURES', d))
    for feature in features:
        if feature not in valid_features:
            bb.fatal("'%s' in IMAGE_FEATURES is not a valid image feature. Valid features: %s" % (feature, ' '.join(valid_features)))

IMAGE_INSTALL ?= ""
IMAGE_INSTALL[type] = "list"
export PACKAGE_INSTALL ?= "${IMAGE_INSTALL} ${ROOTFS_BOOTSTRAP_INSTALL} ${FEATURE_INSTALL}"
PACKAGE_INSTALL_ATTEMPTONLY ?= "${FEATURE_INSTALL_OPTIONAL}"

# Images are generally built explicitly, do not need to be part of world.
EXCLUDE_FROM_WORLD = "1"

USE_DEVFS ?= "0"

PID = "${@os.getpid()}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LDCONFIGDEPEND ?= "ldconfig-native:do_populate_sysroot"
LDCONFIGDEPEND_libc-uclibc = ""

do_rootfs[depends] += "makedevs-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot ${LDCONFIGDEPEND}"
do_rootfs[depends] += "virtual/update-alternatives-native:do_populate_sysroot update-rc.d-native:do_populate_sysroot"
do_rootfs[recrdeptask] += "do_packagedata"

IMAGE_TYPE_live = '${@base_contains("IMAGE_FSTYPES", "live", "live", "empty", d)}'
inherit image-${IMAGE_TYPE_live}
IMAGE_TYPE_vmdk = '${@base_contains("IMAGE_FSTYPES", "vmdk", "vmdk", "empty", d)}'
inherit image-${IMAGE_TYPE_vmdk}

python () {
    deps = " " + imagetypes_getdepends(d)
    d.appendVarFlag('do_rootfs', 'depends', deps)

    deps = ""
    for dep in (d.getVar('EXTRA_IMAGEDEPENDS', True) or "").split():
        deps += " %s:do_populate_sysroot" % dep
    d.appendVarFlag('do_build', 'depends', deps)

    #process IMAGE_FEATURES, we must do this before runtime_mapping_rename
    #Check for replaces image features
    features = set(oe.data.typed_value('IMAGE_FEATURES', d))
    remain_features = features.copy()
    for feature in features:
        replaces = set((d.getVar("IMAGE_FEATURES_REPLACES_%s" % feature, True) or "").split())
        remain_features -= replaces

    #Check for conflict image features
    for feature in remain_features:
        conflicts = set((d.getVar("IMAGE_FEATURES_CONFLICTS_%s" % feature, True) or "").split())
        temp = conflicts & remain_features
        if temp:
            bb.fatal("%s contains conflicting IMAGE_FEATURES %s %s" % (d.getVar('PN', True), feature, ' '.join(list(temp))))

    d.setVar('IMAGE_FEATURES', ' '.join(list(remain_features)))

    # Ensure we have the vendor list for complementary package handling
    ml_vendor_list = ""
    multilibs = d.getVar('MULTILIBS', True) or ""
    for ext in multilibs.split():
        eext = ext.split(':')
        if len(eext) > 1 and eext[0] == 'multilib':
            localdata = bb.data.createCopy(d)
            vendor = localdata.getVar("TARGET_VENDOR_virtclass-multilib-" + eext[1], False)
            ml_vendor_list += " " + vendor
    d.setVar('MULTILIB_VENDORS', ml_vendor_list)

    check_image_features(d)
    initramfs_image = d.getVar('INITRAMFS_IMAGE', True) or ""
    if initramfs_image != "":
        d.appendVarFlag('do_build', 'depends', " %s:do_bundle_initramfs" %  d.getVar('PN', True))
        d.appendVarFlag('do_bundle_initramfs', 'depends', " %s:do_rootfs" % initramfs_image)
}

#
# Get a list of files containing device tables to create.
# * IMAGE_DEVICE_TABLE is the old name to an absolute path to a device table file
# * IMAGE_DEVICE_TABLES is a new name for a file, or list of files, seached
#   for in the BBPATH
# If neither are specified then the default name of files/device_table-minimal.txt
# is searched for in the BBPATH (same as the old version.)
#
def get_devtable_list(d):
    devtable = d.getVar('IMAGE_DEVICE_TABLE', True)
    if devtable != None:
        return devtable
    str = ""
    devtables = d.getVar('IMAGE_DEVICE_TABLES', True)
    if devtables == None:
        devtables = 'files/device_table-minimal.txt'
    for devtable in devtables.split():
        str += " %s" % bb.utils.which(d.getVar('BBPATH', True), devtable)
    return str

IMAGE_CLASSES ?= "image_types"
inherit ${IMAGE_CLASSES}

IMAGE_POSTPROCESS_COMMAND ?= ""
MACHINE_POSTPROCESS_COMMAND ?= ""
ROOTFS_POSTPROCESS_COMMAND_prepend = "run_intercept_scriptlets; "
# Allow dropbear/openssh to accept logins from accounts with an empty password string if debug-tweaks is enabled
ROOTFS_POSTPROCESS_COMMAND += '${@base_contains("IMAGE_FEATURES", "debug-tweaks", "ssh_allow_empty_password; ", "",d)}'
# Enable postinst logging if debug-tweaks is enabled
ROOTFS_POSTPROCESS_COMMAND += '${@base_contains("IMAGE_FEATURES", "debug-tweaks", "postinst_enable_logging; ", "",d)}'
# Set default postinst log file
POSTINST_LOGFILE ?= "${localstatedir}/log/postinstall.log"

# some default locales
IMAGE_LINGUAS ?= "de-de fr-fr en-gb"

LINGUAS_INSTALL ?= "${@" ".join(map(lambda s: "locale-base-%s" % s, d.getVar('IMAGE_LINGUAS', True).split()))}"

PSEUDO_PASSWD = "${IMAGE_ROOTFS}"

do_rootfs[dirs] = "${TOPDIR} ${WORKDIR}/intercept_scripts"
do_rootfs[lockfiles] += "${IMAGE_ROOTFS}.lock"
do_rootfs[cleandirs] += "${S} ${WORKDIR}/intercept_scripts"

# Must call real_do_rootfs() from inside here, rather than as a separate
# task, so that we have a single fakeroot context for the whole process.
do_rootfs[umask] = "022"


run_intercept_scriptlets () {
	if [ -d ${WORKDIR}/intercept_scripts ]; then
		cd ${WORKDIR}/intercept_scripts
		echo "Running intercept scripts:"
		for script in *; do
			[ "$script" = "*" ] && break
			[ "$script" = "postinst_intercept" ] || [ ! -x "$script" ] && continue
			echo "> Executing $script"
			./$script && continue
			echo "WARNING: intercept script \"$script\" failed, falling back to running postinstalls at first boot"
			#
			# If we got here, than the intercept has failed. Next, we must
			# mark the postinstalls as "unpacked". For rpm is a little bit
			# different, we just have to save the package postinstalls in
			# /etc/rpm-postinsts
			#
			pkgs="$(cat ./$script|grep "^##PKGS"|cut -d':' -f2)" || continue
			case ${IMAGE_PKGTYPE} in
				"rpm")
					[ -d ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts/ ] || mkdir ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts/
					v_expr=$(echo ${MULTILIB_GLOBAL_VARIANTS}|tr ' ' '|')
					for p in $pkgs; do
						# remove any multilib prefix from the package name (RPM 
						# does not use it like this)
						new_p=$(echo $p | sed -r "s/^($v_expr)-//")

						# extract the postinstall scriptlet from rpm package and
						# save it in /etc/rpm-postinsts
						echo "  * postponing $new_p"
						rpm -q --scripts --root=${IMAGE_ROOTFS} --dbpath=/var/lib/rpm $new_p |\
						sed -n -e '/^postinstall scriptlet (using .*):$/,/^.* scriptlet (using .*):$/ {/.*/p}' |\
						sed -e 's/postinstall scriptlet (using \(.*\)):$/#!\1/' -e '/^.* scriptlet (using .*):$/d'\
							> ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts/$new_p
						chmod +x ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts/$new_p
					done
					# move to the next intercept script
					continue
					;;
				"ipk")
					status_file="${IMAGE_ROOTFS}${OPKGLIBDIR}/opkg/status"
					;;
				"deb")
					status_file="${IMAGE_ROOTFS}/var/lib/dpkg/status"
					;;
			esac
			# the next piece of code is run only for ipk/dpkg
			sed_expr=""
			for p in $pkgs; do
				echo "  * postponing $p"
				sed_expr="$sed_expr -e \"/^Package: ${p}$/,/^Status: install.* installed$/ {s/installed/unpacked/}\""
			done
			eval sed -i $sed_expr $status_file
		done
	fi
}

# A hook function to support read-only-rootfs IMAGE_FEATURES
# Currently, it only supports sysvinit system.
read_only_rootfs_hook () {
	if ${@base_contains("DISTRO_FEATURES", "sysvinit", "true", "false", d)}; then
	        # Tweak the mount option and fs_passno for rootfs in fstab
		sed -i -e '/^[#[:space:]]*rootfs/{s/defaults/ro/;s/\([[:space:]]*[[:digit:]]\)\([[:space:]]*\)[[:digit:]]$/\1\20/}' ${IMAGE_ROOTFS}/etc/fstab
	        # Change the value of ROOTFS_READ_ONLY in /etc/default/rcS to yes
		if [ -e ${IMAGE_ROOTFS}/etc/default/rcS ]; then
			sed -i 's/ROOTFS_READ_ONLY=no/ROOTFS_READ_ONLY=yes/' ${IMAGE_ROOTFS}/etc/default/rcS
		fi
	        # Run populate-volatile.sh at rootfs time to set up basic files
	        # and directories to support read-only rootfs.
		if [ -x ${IMAGE_ROOTFS}/etc/init.d/populate-volatile.sh ]; then
			${IMAGE_ROOTFS}/etc/init.d/populate-volatile.sh
		fi
		# If we're using openssh and the /etc/ssh directory has no pre-generated keys,
		# we should configure openssh to use the configuration file /etc/ssh/sshd_config_readonly
		# and the keys under /var/run/ssh.
		if [ -d ${IMAGE_ROOTFS}/etc/ssh ]; then
			if [ -e ${IMAGE_ROOTFS}/etc/ssh/ssh_host_rsa_key ]; then
				echo "SYSCONFDIR=/etc/ssh" >> ${IMAGE_ROOTFS}/etc/default/ssh
				echo "SSHD_OPTS=" >> ${IMAGE_ROOTFS}/etc/default/ssh
			else
				echo "SYSCONFDIR=/var/run/ssh" >> ${IMAGE_ROOTFS}/etc/default/ssh
				echo "SSHD_OPTS='-f /etc/ssh/sshd_config_readonly'" >> ${IMAGE_ROOTFS}/etc/default/ssh
			fi
		fi
	fi
}

PACKAGE_EXCLUDE ??= ""
PACKAGE_EXCLUDE[type] = "list"

python rootfs_process_ignore() {
    excl_pkgs = d.getVar("PACKAGE_EXCLUDE", True).split()
    inst_pkgs = d.getVar("PACKAGE_INSTALL", True).split()
    inst_attempt_pkgs = d.getVar("PACKAGE_INSTALL_ATTEMPTONLY", True).split()

    d.setVar('PACKAGE_INSTALL_ORIG', ' '.join(inst_pkgs))
    d.setVar('PACKAGE_INSTALL_ATTEMPTONLY', ' '.join(inst_attempt_pkgs))

    for pkg in excl_pkgs:
        if pkg in inst_pkgs:
            bb.warn("Package %s, set to be excluded, is in %s PACKAGE_INSTALL (%s).  It will be removed from the list." % (pkg, d.getVar('PN', True), inst_pkgs))
            inst_pkgs.remove(pkg)

        if pkg in inst_attempt_pkgs:
            bb.warn("Package %s, set to be excluded, is in %s PACKAGE_INSTALL_ATTEMPTONLY (%s).  It will be removed from the list." % (pkg, d.getVar('PN', True), inst_pkgs))
            inst_attempt_pkgs.remove(pkg)

    d.setVar("PACKAGE_INSTALL", ' '.join(inst_pkgs))
    d.setVar("PACKAGE_INSTALL_ATTEMPTONLY", ' '.join(inst_attempt_pkgs))
}
do_rootfs[prefuncs] += "rootfs_process_ignore"

# We have to delay the runtime_mapping_rename until just before rootfs runs
# otherwise, the multilib renaming could step in and squash any fixups that
# may have occurred.
python rootfs_runtime_mapping() {
    pn = d.getVar('PN', True)
    runtime_mapping_rename("PACKAGE_INSTALL", pn, d)
    runtime_mapping_rename("PACKAGE_INSTALL_ATTEMPTONLY", pn, d)
    runtime_mapping_rename("BAD_RECOMMENDATIONS", pn, d)
}
do_rootfs[prefuncs] += "rootfs_runtime_mapping"

fakeroot do_rootfs () {
	#set -x
	# When use the rpm incremental image generation, don't remove the rootfs
	if [ "${INC_RPM_IMAGE_GEN}" != "1" -o "${IMAGE_PKGTYPE}" != "rpm" ]; then
		rm -rf ${IMAGE_ROOTFS}
	elif [ -d ${T}/saved_rpmlib/var/lib/rpm ]; then
		# Move the rpmlib back
		if [ ! -d ${IMAGE_ROOTFS}/var/lib/rpm ]; then
			mkdir -p ${IMAGE_ROOTFS}/var/lib/
			mv ${T}/saved_rpmlib/var/lib/rpm ${IMAGE_ROOTFS}/var/lib/
		fi
	fi
	rm -rf ${MULTILIB_TEMP_ROOTFS}
	mkdir -p ${IMAGE_ROOTFS}
	mkdir -p ${DEPLOY_DIR_IMAGE}

	cp ${COREBASE}/meta/files/deploydir_readme.txt ${DEPLOY_DIR_IMAGE}/README_-_DO_NOT_DELETE_FILES_IN_THIS_DIRECTORY.txt || true

	# copy the intercept scripts
	cp ${COREBASE}/scripts/postinst-intercepts/* ${WORKDIR}/intercept_scripts/

	rootfs_${IMAGE_PKGTYPE}_do_rootfs

	if [ "${USE_DEVFS}" != "1" ]; then
		for devtable in ${@get_devtable_list(d)}; do
			# Always return ture since there maybe already one when use the
			# incremental image generation
			makedevs -r ${IMAGE_ROOTFS} -D $devtable
		done
	fi

	# remove unneeded packages/files from the final image
	rootfs_uninstall_unneeded

	insert_feed_uris

	if [ "x${LDCONFIGDEPEND}" != "x" ]; then
		# Run ldconfig on the image to create a valid cache 
		# (new format for cross arch compatibility)
		echo executing: ldconfig -r ${IMAGE_ROOTFS} -c new -v
		ldconfig -r ${IMAGE_ROOTFS} -c new -v
	fi

	# (re)create kernel modules dependencies
	# This part is done by kernel-module-* postinstall scripts but if image do
	# not contains modules at all there are few moments in boot sequence with
	# "unable to open modules.dep" message.
	if [ -e ${STAGING_KERNEL_DIR}/kernel-abiversion ]; then
		KERNEL_VERSION=`cat ${STAGING_KERNEL_DIR}/kernel-abiversion`

		mkdir -p ${IMAGE_ROOTFS}/lib/modules/$KERNEL_VERSION
		depmodwrapper -a -b ${IMAGE_ROOTFS} $KERNEL_VERSION
	fi

	${IMAGE_PREPROCESS_COMMAND}

	${@get_imagecmds(d)}

	${IMAGE_POSTPROCESS_COMMAND}
	
	${MACHINE_POSTPROCESS_COMMAND}
}

insert_feed_uris () {
	
	echo "Building feeds for [${DISTRO}].."

	for line in ${FEED_URIS}
	do
		# strip leading and trailing spaces/tabs, then split into name and uri
		line_clean="`echo "$line"|sed 's/^[ \t]*//;s/[ \t]*$//'`"
		feed_name="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\1/p'`"
		feed_uri="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\2/p'`"
		
		echo "Added $feed_name feed with URL $feed_uri"
		
		# insert new feed-sources
		echo "src/gz $feed_name $feed_uri" >> ${IMAGE_ROOTFS}/etc/opkg/${feed_name}-feed.conf
	done
}

log_check() {
	for target in $*
	do
		lf_path="`dirname ${BB_LOGFILE}`/log.do_$target.${PID}"
		
		echo "log_check: Using $lf_path as logfile"
		
		if test -e "$lf_path"
		then
			${IMAGE_PKGTYPE}_log_check $target $lf_path
		else
			echo "Cannot find logfile [$lf_path]"
		fi
		echo "Logfile is clean"
	done
}

MULTILIBRE_ALLOW_REP =. "${base_bindir}|${base_sbindir}|${bindir}|${sbindir}|${libexecdir}|"
MULTILIB_CHECK_FILE = "${WORKDIR}/multilib_check.py"
MULTILIB_TEMP_ROOTFS = "${WORKDIR}/multilib"

multilib_generate_python_file() {
  cat >${MULTILIB_CHECK_FILE} <<EOF
import sys, os, os.path
import re,filecmp

allow_rep=re.compile(re.sub("\|$","","${MULTILIBRE_ALLOW_REP}"))
error_prompt="Multilib check error:"

files={}
dirs=raw_input()
for dir in dirs.split():
  for root, subfolders, subfiles in os.walk(dir):
    for file in subfiles:
      item=os.path.join(root,file)
      key=str(os.path.join("/",os.path.relpath(item,dir)))

      valid=True;
      if key in files:
        #check whether the file is allow to replace
        if allow_rep.match(key):
          valid=True
        else:
          if not filecmp.cmp(files[key],item):
             valid=False
             print("%s duplicate files %s %s is not the same\n" % (error_prompt, item, files[key]))
             sys.exit(1)

      #pass the check, add to list
      if valid:
        files[key]=item
EOF
}

multilib_sanity_check() {
  multilib_generate_python_file
  echo $@ | python ${MULTILIB_CHECK_FILE}
}

get_split_linguas() {
    for translation in ${IMAGE_LINGUAS}; do
        translation_split=$(echo ${translation} | awk -F '-' '{print $1}')
        echo ${translation}
        echo ${translation_split}
    done | sort | uniq
}

rootfs_install_complementary() {
    # Install complementary packages based upon the list of currently installed packages
    # e.g. locales, *-dev, *-dbg, etc. This will only attempt to install these packages,
    # if they don't exist then no error will occur.
    # Note: every backend needs to call this function explicitly after the normal
    # package installation

    # Get list of installed packages
    list_installed_packages arch > ${WORKDIR}/installed_pkgs.txt

    # Apply the globs to all the packages currently installed
    if [ -n "$1" -a "$1" = "populate_sdk" ] ; then
        GLOBS="${SDKIMAGE_INSTALL_COMPLEMENTARY}"
    elif [ -n "$1" ]; then
        GLOBS="$@"
    else
        GLOBS="${IMAGE_INSTALL_COMPLEMENTARY}"
        # Add locales
        SPLIT_LINGUAS=`get_split_linguas`
        PACKAGES_TO_INSTALL=""
        for lang in $SPLIT_LINGUAS ; do
            GLOBS="$GLOBS *-locale-$lang"
        done
    fi

    if [ "$GLOBS" != "" ] ; then
        # Use the magic script to do all the work for us :)
        : > ${WORKDIR}/complementary_pkgs.txt
        for vendor in '${TARGET_VENDOR}' ${MULTILIB_VENDORS} ; do
            oe-pkgdata-util glob ${TMPDIR}/pkgdata $vendor-${TARGET_OS} ${WORKDIR}/installed_pkgs.txt "$GLOBS" >> ${WORKDIR}/complementary_pkgs.txt
        done

        # Install the packages, if any
        sed -i '/^$/d' ${WORKDIR}/complementary_pkgs.txt
        if [ -s ${WORKDIR}/complementary_pkgs.txt ]; then
            echo "Installing complementary packages"
            rootfs_install_packages ${WORKDIR}/complementary_pkgs.txt
        fi
    fi

    # Workaround for broken shell function dependencies
    if false ; then
        get_split_linguas
    fi
}

rootfs_uninstall_unneeded () {
	if ${@base_contains("IMAGE_FEATURES", "package-management", "false", "true", d)}; then
		if [ -z "$(delayed_postinsts)" ]; then
			# All packages were successfully configured.
			# update-rc.d, base-passwd, run-postinsts are no further use, remove them now
			remove_run_postinsts=false
			if [ -e ${IMAGE_ROOTFS}${sysconfdir}/init.d/run-postinsts ]; then
				remove_run_postinsts=true
			fi
			rootfs_uninstall_packages update-rc.d base-passwd ${ROOTFS_BOOTSTRAP_INSTALL}

			# Need to remove rc.d files for run-postinsts by hand since opkg won't
			# call postrm scripts in offline root mode.
			if $remove_run_postinsts; then
				update-rc.d -f -r ${IMAGE_ROOTFS} run-postinsts remove
			fi
		else
			# Some packages were not successfully configured, save them only
			# if we have run-postinsts script present. Otherwise, they're
			# useless
			if [ -e ${IMAGE_ROOTFS}${sysconfdir}/init.d/run-postinsts ]; then
				save_postinsts
			fi
		fi

		# Since no package manager is present in the image the metadata is not needed
		remove_packaging_data_files
	fi
}

# set '*' as the root password so the images
# can decide if they want it or not
zap_root_password () {
	sed 's%^root:[^:]*:%root:*:%' < ${IMAGE_ROOTFS}/etc/passwd >${IMAGE_ROOTFS}/etc/passwd.new
	mv ${IMAGE_ROOTFS}/etc/passwd.new ${IMAGE_ROOTFS}/etc/passwd
} 

# allow dropbear/openssh to accept root logins and logins from accounts with an empty password string
ssh_allow_empty_password () {
	if [ -e ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config ]; then
		sed -i 's#.*PermitRootLogin.*#PermitRootLogin yes#' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
		sed -i 's#.*PermitEmptyPasswords.*#PermitEmptyPasswords yes#' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
	fi

	if [ -e ${IMAGE_ROOTFS}${sbindir}/dropbear ] ; then
		if grep -q DROPBEAR_EXTRA_ARGS ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear 2>/dev/null ; then
			if ! grep -q "DROPBEAR_EXTRA_ARGS=.*-B" ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear ; then
				sed -i 's/^DROPBEAR_EXTRA_ARGS="*\([^"]*\)"*/DROPBEAR_EXTRA_ARGS="\1 -B"/' ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear
			fi
		else
			printf '\nDROPBEAR_EXTRA_ARGS="-B"\n' >> ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear
		fi
	fi
}

# Enable postinst logging if debug-tweaks is enabled
postinst_enable_logging () {
	mkdir -p ${IMAGE_ROOTFS}${sysconfdir}/default
	echo "POSTINST_LOGGING=1" >> ${IMAGE_ROOTFS}${sysconfdir}/default/postinst
	echo "LOGFILE=${POSTINST_LOGFILE}" >> ${IMAGE_ROOTFS}${sysconfdir}/default/postinst
}

# Turn any symbolic /sbin/init link into a file
remove_init_link () {
	if [ -h ${IMAGE_ROOTFS}/sbin/init ]; then
		LINKFILE=${IMAGE_ROOTFS}`readlink ${IMAGE_ROOTFS}/sbin/init`
		rm ${IMAGE_ROOTFS}/sbin/init
		cp $LINKFILE ${IMAGE_ROOTFS}/sbin/init
	fi
}

make_zimage_symlink_relative () {
	if [ -L ${IMAGE_ROOTFS}/boot/zImage ]; then
		(cd ${IMAGE_ROOTFS}/boot/ && for i in `ls zImage-* | sort`; do ln -sf $i zImage; done)
	fi
}

write_image_manifest () {
	rootfs_${IMAGE_PKGTYPE}_write_manifest

	if [ -n "${IMAGE_LINK_NAME}" ]; then
		rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.manifest
		ln -s ${IMAGE_NAME}.rootfs.manifest ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.manifest
	fi
}

# Make login manager(s) enable automatic login.
# Useful for devices where we do not want to log in at all (e.g. phones)
set_image_autologin () {
        sed -i 's%^AUTOLOGIN=\"false"%AUTOLOGIN="true"%g' ${IMAGE_ROOTFS}/etc/sysconfig/gpelogin
}

# Can be use to create /etc/timestamp during image construction to give a reasonably 
# sane default time setting
rootfs_update_timestamp () {
	date -u +%4Y%2m%2d%2H%2M >${IMAGE_ROOTFS}/etc/timestamp
}

# Prevent X from being started
rootfs_no_x_startup () {
	if [ -f ${IMAGE_ROOTFS}/etc/init.d/xserver-nodm ]; then
		chmod a-x ${IMAGE_ROOTFS}/etc/init.d/xserver-nodm
	fi
}

rootfs_trim_schemas () {
	for schema in ${IMAGE_ROOTFS}/etc/gconf/schemas/*.schemas
	do
		# Need this in case no files exist
		if [ -e $schema ]; then
			oe-trim-schemas $schema > $schema.new
			mv $schema.new $schema
		fi
	done
}

EXPORT_FUNCTIONS zap_root_password remove_init_link do_rootfs make_zimage_symlink_relative set_image_autologin rootfs_update_timestamp rootfs_no_x_startup

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
do_package[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_package_write_rpm[noexec] = "1"

addtask rootfs before do_build
# Allow the kernel to be repacked with the initramfs and boot image file as a single file
do_bundle_initramfs[depends] += "virtual/kernel:do_bundle_initramfs"
do_bundle_initramfs[nostamp] = "1"
do_bundle_initramfs[noexec] = "1"
do_bundle_initramfs () {
	:
}
addtask bundle_initramfs after do_rootfs
