inherit meta toolchain-scripts
inherit populate_sdk_${IMAGE_PKGTYPE}

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${TMPDIR}/deploy/sdk"

SDKTARGETSYSROOT = "${SDKPATH}/sysroots/${MULTIMACH_TARGET_SYS}"

TOOLCHAIN_HOST_TASK ?= "nativesdk-packagegroup-sdk-host packagegroup-cross-canadian-${TRANSLATED_TARGET_ARCH}"
TOOLCHAIN_HOST_TASK_ATTEMPTONLY ?= ""
TOOLCHAIN_TARGET_TASK ?= "packagegroup-core-standalone-sdk-target packagegroup-core-standalone-sdk-target-dbg"
TOOLCHAIN_TARGET_TASK_ATTEMPTONLY ?= ""
TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-toolchain-${DISTRO_VERSION}"

SDK_RDEPENDS = "${TOOLCHAIN_TARGET_TASK} ${TOOLCHAIN_HOST_TASK}"
SDK_DEPENDS = "virtual/fakeroot-native sed-native"

# We want the MULTIARCH_TARGET_SYS to point to the TUNE_PKGARCH, not PACKAGE_ARCH as it
# could be set to the MACHINE_ARCH
REAL_MULTIMACH_TARGET_SYS = "${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"

PID = "${@os.getpid()}"

EXCLUDE_FROM_WORLD = "1"

SDK_PACKAGING_FUNC ?= "create_shar"

python () {
    # If we don't do this we try and run the mapping hooks while parsing which is slow
    # bitbake should really provide something to let us know this...
    if bb.data.getVar('BB_WORKERCONTEXT', d, True) is not None:
        runtime_mapping_rename("TOOLCHAIN_TARGET_TASK", d)
}

fakeroot python do_populate_sdk() {
    bb.build.exec_func("populate_sdk_image", d)

    # Handle multilibs in the SDK environment, siteconfig, etc files...
    localdata = bb.data.createCopy(d)

    # make sure we only use the WORKDIR value from 'd', or it can change
    localdata.setVar('WORKDIR', d.getVar('WORKDIR', True))

    # make sure we only use the SDKTARGETSYSROOT value from 'd'
    localdata.setVar('SDKTARGETSYSROOT', d.getVar('SDKTARGETSYSROOT', True))

    # Process DEFAULTTUNE
    bb.build.exec_func("create_sdk_files", localdata)

    variants = d.getVar("MULTILIB_VARIANTS", True) or ""
    for item in variants.split():
        # Load overrides from 'd' to avoid having to reset the value...
        overrides = d.getVar("OVERRIDES", False) + ":virtclass-multilib-" + item
        localdata.setVar("OVERRIDES", overrides)
        bb.data.update_data(localdata)
        bb.build.exec_func("create_sdk_files", localdata)

    bb.build.exec_func("tar_sdk", d)

    bb.build.exec_func(d.getVar("SDK_PACKAGING_FUNC", True), d)
}

fakeroot populate_sdk_image() {
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
}

fakeroot create_sdk_files() {
	# Setup site file for external use
	toolchain_create_sdk_siteconfig ${SDK_OUTPUT}/${SDKPATH}/site-config-${REAL_MULTIMACH_TARGET_SYS}

	toolchain_create_sdk_env_script ${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}

	# Add version information
	toolchain_create_sdk_version ${SDK_OUTPUT}/${SDKPATH}/version-${REAL_MULTIMACH_TARGET_SYS}

	cp ${COREBASE}/scripts/relocate_sdk.py ${SDK_OUTPUT}/${SDKPATH}/

	# Replace the ##DEFAULT_INSTALL_DIR## with the correct pattern.
	# Escape special characters like '+' and '.' in the SDKPATH
	escaped_sdkpath=$(echo ${SDKPATH} |sed -e "s:[\+\.]:\\\\\\\\\0:g")
	sed -i -e "s:##DEFAULT_INSTALL_DIR##:$escaped_sdkpath:" ${SDK_OUTPUT}/${SDKPATH}/relocate_sdk.py
}

fakeroot tar_sdk() {
	# Package it up
	mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}
	tar --owner=root --group=root -cj --file=${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 .
}

fakeroot create_shar() {
	cat << "EOF" > ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.sh
#!/bin/bash

DEFAULT_INSTALL_DIR="${SDKPATH}"

printf "Enter target directory for SDK (default: $DEFAULT_INSTALL_DIR): "
read target_sdk_dir

if [ "$target_sdk_dir" = "" ]; then
	target_sdk_dir=$DEFAULT_INSTALL_DIR
fi

eval target_sdk_dir=$target_sdk_dir
if [ -d $target_sdk_dir ]; then
	target_sdk_dir=$(cd $target_sdk_dir; pwd)
else
	target_sdk_dir=$(readlink -m $target_sdk_dir)
fi

printf "You are about to install the SDK to \"$target_sdk_dir\". Proceed[Y/n]?"
read answer

if [ "$answer" = "" ]; then
	answer="y"
fi

if [ "$answer" != "Y" -a "$answer" != "y" ]; then
	echo "Installation aborted!"
	exit 1
fi

mkdir -p $target_sdk_dir >/dev/null 2>&1
if [ $? -ne 0 ]; then
	echo "Error: Unable to create target directory. Do you have permissions?"
	exit 1
fi

payload_offset=$(($(grep -na -m1 "^MARKER:$" $0|cut -d':' -f1) + 1))

printf "Extracting SDK..."
tail -n +$payload_offset $0| tar xj --strip-components=4 -C $target_sdk_dir
echo "done"

printf "Setting it up..."
# fix environment paths
env_setup_script=$(find $target_sdk_dir/ -name "environment-setup-${REAL_MULTIMACH_TARGET_SYS}")
sed -e "s:$DEFAULT_INSTALL_DIR:$target_sdk_dir:g" -i $env_setup_script

# fix dynamic loader paths in all ELF SDK binaries
native_sysroot=$(cat $env_setup_script |grep OECORE_NATIVE_SYSROOT|cut -d'=' -f2|tr -d '"')
dl_path=$(find $native_sysroot/lib -name "ld-linux*")
executable_files=$(find $native_sysroot -type f -perm +111)
${env_setup_script%/*}/relocate_sdk.py $target_sdk_dir $dl_path $executable_files
if [ $? -ne 0 ]; then
	echo "SDK could not be set up. Relocate script failed. Abort!"
	exit 1
fi

# replace ${SDKPATH} with the new prefix in all text files: configs/scripts/etc
find $native_sysroot -type f -exec file '{}' \;|grep ":.*ASCII.*text"|cut -d':' -f1|xargs sed -i -e "s:$DEFAULT_INSTALL_DIR:$target_sdk_dir:g"

# change all symlinks pointing to ${SDKPATH}
for l in $(find $native_sysroot -type l); do
	ln -sf $(readlink $l|sed -e "s:$DEFAULT_INSTALL_DIR:$target_sdk_dir:") $l
done

echo done

# delete the relocating script, so that user is forced to re-run the installer
# if he/she wants another location for the sdk
rm ${env_setup_script%/*}/relocate_sdk.py

echo "SDK has been successfully set up and is ready to be used."

exit 0

MARKER:
EOF
	# append the SDK tarball
	cat ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 >> ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.sh

	# delete the old tarball, we don't need it anymore
	rm ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2
}

populate_sdk_log_check() {
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

do_populate_sdk[dirs] = "${TOPDIR}"
do_populate_sdk[nostamp] = "1"
do_populate_sdk[depends] += "${@' '.join([x + ':do_populate_sysroot' for x in d.getVar('SDK_DEPENDS', True).split()])}"
do_populate_sdk[rdepends] = "${@' '.join([x + ':do_populate_sysroot' for x in d.getVar('SDK_RDEPENDS', True).split()])}"
do_populate_sdk[recrdeptask] = "do_package_write"
addtask populate_sdk
