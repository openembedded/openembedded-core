#
# Creates a root filesystem out of rpm packages
#

ROOTFS_PKGMANAGE = "rpm zypper"

# Add 50Meg of extra space for zypper database space
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@base_contains("PACKAGE_INSTALL", "zypper", " + 51200", "" ,d)}"

# Postinstalls on device are handled within this class at present
ROOTFS_PKGMANAGE_BOOTSTRAP = ""

do_rootfs[depends] += "rpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_rootfs[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
#do_rootfs[depends] += "createrepo-native:do_populate_sysroot"

do_rootfs[recrdeptask] += "do_package_write_rpm"

RPM_PREPROCESS_COMMANDS = "package_update_index_rpm; package_generate_rpm_conf; "
RPM_POSTPROCESS_COMMANDS = "rootfs_install_all_locales; "

# 
# Allow distributions to alter when [postponed] package install scripts are run
#
POSTINSTALL_INITPOSITION ?= "98"

rpmlibdir = "/var/lib/rpm"
opkglibdir = "${localstatedir}/lib/opkg"

RPMOPTS="--dbpath ${rpmlibdir} --define='_openall_before_chroot 1'"
RPM="rpm ${RPMOPTS}"

# RPM doesn't work with multiple rootfs generation at once due to collisions in the use of files 
# in ${DEPLOY_DIR_RPM}. This can be removed if package_update_index_rpm can be called concurrently
do_rootfs[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

fakeroot rootfs_rpm_do_rootfs () {
	set +x

	${RPM_PREPROCESS_COMMANDS}

	#createrepo "${DEPLOY_DIR_RPM}"

	# install packages
	# This needs to work in the same way as populate_sdk_rpm.bbclass!
	export INSTALL_ROOTFS_RPM="${IMAGE_ROOTFS}"
	export INSTALL_PLATFORM_RPM="${TARGET_ARCH}"
	export INSTALL_CONFBASE_RPM="${RPMCONF_TARGET_BASE}"
	export INSTALL_PACKAGES_RPM="${PACKAGE_INSTALL}"
	export INSTALL_PACKAGES_ATTEMPTONLY_RPM="${PACKAGE_INSTALL_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_RPM="${LINGUAS_INSTALL}"
	export INSTALL_PROVIDENAME_RPM=""
	export INSTALL_TASK_RPM="rootfs_rpm_do_rootfs"

	# Setup base system configuration
	mkdir -p ${INSTALL_ROOTFS_RPM}/etc/rpm/

	mkdir -p ${INSTALL_ROOTFS_RPM}${rpmlibdir}
	mkdir -p ${INSTALL_ROOTFS_RPM}${rpmlibdir}/log
	# After change the __db.* cache size, log file will not be generated automatically,
	# that will raise some warnings, so touch a bare log for rpm write into it.
	touch ${INSTALL_ROOTFS_RPM}${rpmlibdir}/log/log.0000000001
	cat > ${INSTALL_ROOTFS_RPM}${rpmlibdir}/DB_CONFIG << EOF
# ================ Environment
set_data_dir            .
set_create_dir          .
set_lg_dir              ./log
set_tmp_dir             ./tmp

# -- thread_count must be >= 8
set_thread_count        64

# ================ Logging

# ================ Memory Pool
set_mp_mmapsize         268435456

# ================ Locking
set_lk_max_locks        16384
set_lk_max_lockers      16384
set_lk_max_objects      16384
mutex_set_max           163840

# ================ Replication
EOF

	# List must be prefered to least preferred order
	INSTALL_PLATFORM_EXTRA_RPM=""
	for each_arch in ${MULTILIB_PACKAGE_ARCHS} ${PACKAGE_ARCHS}; do
		INSTALL_PLATFORM_EXTRA_RPM="$each_arch $INSTALL_PLATFORM_EXTRA_RPM"
	done
	export INSTALL_PLATFORM_RPM

	package_install_internal_rpm

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	${ROOTFS_POSTINSTALL_COMMAND}

	# Report delayed package scriptlets
	for i in ${IMAGE_ROOTFS}/etc/rpm-postinsts/*; do
		echo "Delayed package scriptlet: `head -n 3 $i | tail -n 1`"
	done

	install -d ${IMAGE_ROOTFS}/${sysconfdir}/rcS.d
	# Stop $i getting expanded below...
	i=\$i
	cat > ${IMAGE_ROOTFS}${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts << EOF
#!/bin/sh
for i in /etc/rpm-postinsts/*; do
	echo "Running postinst $i..."
	if [ -f $i ] && $i; then
		rm $i
	else
		echo "ERROR: postinst $i failed."
	fi
done
rm -f ${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
EOF
	chmod 0755 ${IMAGE_ROOTFS}${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	${RPM_POSTPROCESS_COMMANDS}
	${ROOTFS_POSTPROCESS_COMMAND}
	
	rm -rf ${IMAGE_ROOTFS}/var/cache2/
	rm -rf ${IMAGE_ROOTFS}/var/run2/
	rm -rf ${IMAGE_ROOTFS}/var/log2/

	# remove lock files
	rm -f ${IMAGE_ROOTFS}${rpmlibdir}/__db.*

	# Move manifests into the directory with the logs
	mv ${IMAGE_ROOTFS}/install/*.manifest ${T}/

	# Remove all remaining resolver files
	rm -rf ${IMAGE_ROOTFS}/install

	log_check rootfs

	# Workaround so the parser knows we need the resolve_package function!
	if false ; then
		resolve_package_rpm foo ${RPMCONF_TARGET_BASE}.conf || true
	fi
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${rpmlibdir}
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
}

RPM_QUERY_CMD = '${RPM} --root ${IMAGE_ROOTFS} -D "_dbpath ${rpmlibdir}" \
		-D "__dbi_txn create nofsync private"'

list_installed_packages() {
	${RPM_QUERY_CMD} -qa --qf "[%{NAME}\n]"
}

get_package_filename() {
	resolve_package_rpm ${RPMCONF_TARGET_BASE}-base_archs.conf $1
}

list_package_depends() {
	pkglist=`list_installed_packages`

	# REQUIRE* lists "soft" requirements (which we know as recommends and RPM refers to
	# as "suggests") so filter these out with the help of awk
	for req in `${RPM_QUERY_CMD} -q --qf "[%{REQUIRENAME} %{REQUIREFLAGS}\n]" $1 | awk '{ if( and($2, 0x80000) == 0) print $1 }'`; do
		if echo "$req" | grep -q "^rpmlib" ; then continue ; fi

		realpkg=""
		for dep in $pkglist; do
			if [ "$dep" = "$req" ] ; then
				realpkg="1"
				echo $req
				break
			fi
		done

		if [ "$realdep" = "" ] ; then
			${RPM_QUERY_CMD} -q --whatprovides $req --qf "%{NAME}\n"
		fi
	done
}

list_package_recommends() {
	${RPM_QUERY_CMD} -q --suggests $1
}

rootfs_check_package_exists() {
	resolve_package_rpm ${RPMCONF_TARGET_BASE}-base_archs.conf $1
}

rootfs_install_packages() {
    # The pkg to be installed here is not controlled by the
    # package_install_internal_rpm, so it may have already been
    # installed(e.g, installed in the first time when generate the
    # rootfs), use '--replacepkgs' to always install them
	for pkg in $@; do
		${RPM} --root ${IMAGE_ROOTFS} -D "_dbpath ${rpmlibdir}" \
			-D "__dbi_txn create nofsync private" \
			--noscripts --notriggers --noparentdirs --nolinktos \
			--replacepkgs -Uhv $pkg || true
	done
}

python () {
    if d.getVar('BUILD_IMAGES_FROM_FEEDS', True):
        flags = d.getVarFlag('do_rootfs', 'recrdeptask')
        flags = flags.replace("do_package_write_rpm", "")
        flags = flags.replace("do_deploy", "")
        flags = flags.replace("do_populate_sysroot", "")
        d.setVarFlag('do_rootfs', 'recrdeptask', flags)
        d.setVar('RPM_PREPROCESS_COMMANDS', '')
        d.setVar('RPM_POSTPROCESS_COMMANDS', '')

    ml_package_archs = ""
    ml_prefix_list = ""
    multilibs = d.getVar('MULTILIBS', True) or ""
    for ext in multilibs.split():
        eext = ext.split(':')
        if len(eext) > 1 and eext[0] == 'multilib':
            localdata = bb.data.createCopy(d)
            default_tune = localdata.getVar("DEFAULTTUNE_virtclass-multilib-" + eext[1], False)
            if default_tune:
                localdata.setVar("DEFAULTTUNE", default_tune)
            package_archs = localdata.getVar("PACKAGE_ARCHS", True) or ""
            package_archs = " ".join([i in "all noarch any".split() and i or eext[1]+"_"+i for i in package_archs.split()])
            ml_package_archs += " " + package_archs
            ml_prefix_list += " " + eext[1]
            #bb.note("ML_PACKAGE_ARCHS %s %s %s" % (eext[1], localdata.getVar("PACKAGE_ARCHS", True) or "(none)", overrides))
    d.setVar('MULTILIB_PACKAGE_ARCHS', ml_package_archs)
    d.setVar('MULTILIB_PREFIX_LIST', ml_prefix_list)
}
