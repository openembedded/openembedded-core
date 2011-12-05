inherit meta toolchain-scripts
inherit populate_sdk_${IMAGE_PKGTYPE}

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

SDKTARGETSYSROOT = "${SDKPATH}/sysroots/${MULTIMACH_TARGET_SYS}"

TOOLCHAIN_HOST_TASK ?= "task-sdk-host-nativesdk task-cross-canadian-${TRANSLATED_TARGET_ARCH}"
TOOLCHAIN_TARGET_TASK ?= "task-core-standalone-sdk-target task-core-standalone-sdk-target-dbg"
TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-toolchain-${DISTRO_VERSION}"

RDEPENDS = "${TOOLCHAIN_TARGET_TASK} ${TOOLCHAIN_HOST_TASK}"
DEPENDS = "virtual/fakeroot-native sed-native"

PID = "${@os.getpid()}"

EXCLUDE_FROM_WORLD = "1"

python () {
    # If we don't do this we try and run the mapping hooks while parsing which is slow
    # bitbake should really provide something to let us know this...
    if bb.data.getVar('BB_WORKERCONTEXT', d, True) is not None:
        runtime_mapping_rename("TOOLCHAIN_TARGET_TASK", d)
}

fakeroot do_populate_sdk() {
	rm -rf ${SDK_OUTPUT}
	mkdir -p ${SDK_OUTPUT}

	# populate_sdk_<image> is required to construct two images:
	#  SDK_ARCH-nativesdk - contains the cross compiler and associated tooling
	#  target - contains a target rootfs configured for the SDK usage
	#
	# the output of populate_sdk_<image> should end up in ${SDK_OUTPUT} it is made
	# up of:
	#  ${SDK_OUTPUT}/<sdk_arch-nativesdk pkgs>
	#  ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/<target pkgs>

	populate_sdk_${IMAGE_PKGTYPE}

	# Don't ship any libGL in the SDK
	rm -rf ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/libGL*

	# Can copy pstage files here
	# target_pkgs=`cat ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/var/lib/opkg/status | grep Package: | cut -f 2 -d ' '`

	# Fix or remove broken .la files
	#rm -f ${SDK_OUTPUT}/${SDKPATHNATIVE}/lib/*.la
	rm -f ${SDK_OUTPUT}/${SDKPATHNATIVE}${libdir_nativesdk}/*.la

	# Link the ld.so.cache file into the hosts filesystem
	ln -s /etc/ld.so.cache ${SDK_OUTPUT}/${SDKPATHNATIVE}/etc/ld.so.cache

	# Setup site file for external use
	toolchain_create_sdk_siteconfig ${SDK_OUTPUT}/${SDKPATH}/site-config-${MULTIMACH_TARGET_SYS}

	toolchain_create_sdk_env_script

	# Add version information
	toolchain_create_sdk_version ${SDK_OUTPUT}/${SDKPATH}/version-${MULTIMACH_TARGET_SYS}

	# Package it up
	mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	tar --owner=root --group=root -cj --file=${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 .
}

populate_sdk_log_check() {
	for target in $*
	do
		lf_path="${WORKDIR}/temp/log.do_$target.${PID}"

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

do_populate_sdk[nostamp] = "1"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk before do_build after do_install
