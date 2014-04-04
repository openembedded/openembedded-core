inherit meta toolchain-scripts

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

SDKIMAGE_FEATURES ??= "dev-pkgs dbg-pkgs"
SDKIMAGE_INSTALL_COMPLEMENTARY = '${@complementary_globs("SDKIMAGE_FEATURES", d)}'

inherit populate_sdk_${IMAGE_PKGTYPE}

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDK_DEPLOY = "${DEPLOY_DIR}/sdk"

B_task-populate-sdk = "${SDK_DIR}"

SDKTARGETSYSROOT = "${SDKPATH}/sysroots/${REAL_MULTIMACH_TARGET_SYS}"

TOOLCHAIN_HOST_TASK ?= "nativesdk-packagegroup-sdk-host packagegroup-cross-canadian-${MACHINE}"
TOOLCHAIN_HOST_TASK_ATTEMPTONLY ?= ""
TOOLCHAIN_TARGET_TASK ?= "packagegroup-core-standalone-sdk-target packagegroup-core-standalone-sdk-target-dbg"
TOOLCHAIN_TARGET_TASK_ATTEMPTONLY ?= ""
TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-toolchain-${SDK_VERSION}"

SDK_RDEPENDS = "${TOOLCHAIN_TARGET_TASK} ${TOOLCHAIN_HOST_TASK}"
SDK_DEPENDS = "virtual/fakeroot-native sed-native"

# We want the MULTIARCH_TARGET_SYS to point to the TUNE_PKGARCH, not PACKAGE_ARCH as it
# could be set to the MACHINE_ARCH
REAL_MULTIMACH_TARGET_SYS = "${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"

PID = "${@os.getpid()}"

EXCLUDE_FROM_WORLD = "1"

SDK_PACKAGING_FUNC ?= "create_shar"

fakeroot python do_populate_sdk() {
    from oe.sdk import populate_sdk
    from oe.manifest import create_manifest, Manifest

    pn = d.getVar('PN', True)
    runtime_mapping_rename("TOOLCHAIN_TARGET_TASK", pn, d)

    # create target/host SDK manifests
    create_manifest(d, manifest_dir=d.getVar('SDK_DIR', True),
                    manifest_type=Manifest.MANIFEST_TYPE_SDK_HOST)
    create_manifest(d, manifest_dir=d.getVar('SDK_DIR', True),
                    manifest_type=Manifest.MANIFEST_TYPE_SDK_TARGET)

    populate_sdk(d)

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

SDKTAROPTS = "--owner=root --group=root -j"

fakeroot tar_sdk() {
	# Package it up
	mkdir -p ${SDK_DEPLOY}
	cd ${SDK_OUTPUT}/${SDKPATH}
	tar ${SDKTAROPTS} -c --file=${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.tar.bz2 .
}

fakeroot create_shar() {
	cat << "EOF" > ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.sh
#!/bin/bash

INST_ARCH=$(uname -m | sed -e "s/i[3-6]86/ix86/" -e "s/x86[-_]64/x86_64/")
SDK_ARCH=$(echo ${SDK_ARCH} | sed -e "s/i[3-6]86/ix86/" -e "s/x86[-_]64/x86_64/")

if [ "$INST_ARCH" != "$SDK_ARCH" ]; then
	# Allow for installation of ix86 SDK on x86_64 host
	if [ "$INST_ARCH" != x86_64 -o "$SDK_ARCH" != ix86 ]; then
		echo "Error: Installation machine not supported!"
		exit 1
	fi
fi

DEFAULT_INSTALL_DIR="${SDKPATH}"
SUDO_EXEC=""
target_sdk_dir=""
answer=""
relocate=1
savescripts=0
verbose=0
while getopts ":yd:DRS" OPT; do
	case $OPT in
	y)
		answer="Y"
		[ "$target_sdk_dir" = "" ] && target_sdk_dir=$DEFAULT_INSTALL_DIR
		;;
	d)
		target_sdk_dir=$OPTARG
		;;
	D)
		verbose=1
		;;
	R)
		relocate=0
		savescripts=1
		;;
	S)
		savescripts=1
		;;
	*)
		echo "Usage: $(basename $0) [-y] [-d <dir>]"
		echo "  -y         Automatic yes to all prompts"
		echo "  -d <dir>   Install the SDK to <dir>"
		echo "======== Advanced DEBUGGING ONLY OPTIONS ========"
		echo "  -S         Save relocation scripts"
		echo "  -R         Do not relocate executables"
		echo "  -D         use set -x to see what is going on"
		exit 1
		;;
	esac
done

if [ $verbose = 1 ] ; then
	set -x
fi

printf "Enter target directory for SDK (default: $DEFAULT_INSTALL_DIR): "
if [ "$target_sdk_dir" = "" ]; then
	read target_sdk_dir
	[ "$target_sdk_dir" = "" ] && target_sdk_dir=$DEFAULT_INSTALL_DIR
else
	echo "$target_sdk_dir"
fi

eval target_sdk_dir=$(echo "$target_sdk_dir"|sed 's/ /\\ /g')
if [ -d "$target_sdk_dir" ]; then
	target_sdk_dir=$(cd "$target_sdk_dir"; pwd)
else
	target_sdk_dir=$(readlink -m "$target_sdk_dir")
fi

if [ -n "$(echo $target_sdk_dir|grep ' ')" ]; then
	echo "The target directory path ($target_sdk_dir) contains spaces. Abort!"
	exit 1
fi

if [ -e "$target_sdk_dir/environment-setup-${REAL_MULTIMACH_TARGET_SYS}" ]; then
	echo "The directory \"$target_sdk_dir\" already contains a SDK for this architecture."
	printf "If you continue, existing files will be overwritten! Proceed[y/N]?"

	default_answer="n"
else
	printf "You are about to install the SDK to \"$target_sdk_dir\". Proceed[Y/n]?"

	default_answer="y"
fi

if [ "$answer" = "" ]; then
	read answer
	[ "$answer" = "" ] && answer="$default_answer"
else
	echo $answer
fi

if [ "$answer" != "Y" -a "$answer" != "y" ]; then
	echo "Installation aborted!"
	exit 1
fi

# Try to create the directory (this will not succeed if user doesn't have rights)
mkdir -p $target_sdk_dir >/dev/null 2>&1

# if don't have the right to access dir, gain by sudo 
if [ ! -x $target_sdk_dir -o ! -w $target_sdk_dir -o ! -r $target_sdk_dir ]; then 
	SUDO_EXEC=$(which "sudo")
	if [ -z $SUDO_EXEC ]; then
		echo "No command 'sudo' found, please install sudo first. Abort!"
		exit 1
	fi

	# test sudo could gain root right
	$SUDO_EXEC pwd >/dev/null 2>&1
	[ $? -ne 0 ] && echo "Sorry, you are not allowed to execute as root." && exit 1

	# now that we have sudo rights, create the directory
	$SUDO_EXEC mkdir -p $target_sdk_dir >/dev/null 2>&1
fi

payload_offset=$(($(grep -na -m1 "^MARKER:$" $0|cut -d':' -f1) + 1))

printf "Extracting SDK..."
tail -n +$payload_offset $0| $SUDO_EXEC tar xj -C $target_sdk_dir
echo "done"

printf "Setting it up..."
# fix environment paths
for env_setup_script in `ls $target_sdk_dir/environment-setup-*`; do
	$SUDO_EXEC sed -e "s:$DEFAULT_INSTALL_DIR:$target_sdk_dir:g" -i $env_setup_script
done

# fix dynamic loader paths in all ELF SDK binaries
native_sysroot=$($SUDO_EXEC cat $env_setup_script |grep 'OECORE_NATIVE_SYSROOT='|cut -d'=' -f2|tr -d '"')
dl_path=$($SUDO_EXEC find $native_sysroot/lib -name "ld-linux*")
if [ "$dl_path" = "" ] ; then
	echo "SDK could not be set up. Relocate script unable to find ld-linux.so. Abort!"
	exit 1
fi
executable_files=$($SUDO_EXEC find $native_sysroot -type f -perm /111 -exec file '{}' \;| grep "\(executable\|dynamically linked\)" | cut -f 1 -d ':') 

tdir=`mktemp -d`
if [ x$tdir = x ] ; then
   echo "SDK relocate failed, could not create a temporary directory"
   exit 1
fi
echo "#!/bin/bash" > $tdir/relocate_sdk.sh
echo exec ${env_setup_script%/*}/relocate_sdk.py $target_sdk_dir $dl_path $executable_files >> $tdir/relocate_sdk.sh
$SUDO_EXEC mv $tdir/relocate_sdk.sh ${env_setup_script%/*}/relocate_sdk.sh
$SUDO_EXEC chmod 755 ${env_setup_script%/*}/relocate_sdk.sh
rm -rf $tdir
if [ $relocate = 1 ] ; then
	$SUDO_EXEC ${env_setup_script%/*}/relocate_sdk.sh
	if [ $? -ne 0 ]; then
		echo "SDK could not be set up. Relocate script failed. Abort!"
		exit 1
	fi
fi

# replace ${SDKPATH} with the new prefix in all text files: configs/scripts/etc
$SUDO_EXEC find $native_sysroot -type f -exec file '{}' \;|grep ":.*\(ASCII\|script\|source\).*text"|cut -d':' -f1|$SUDO_EXEC xargs sed -i -e "s:$DEFAULT_INSTALL_DIR:$target_sdk_dir:g"

# change all symlinks pointing to ${SDKPATH}
for l in $($SUDO_EXEC find $native_sysroot -type l); do
	$SUDO_EXEC ln -sfn $(readlink $l|$SUDO_EXEC sed -e "s:$DEFAULT_INSTALL_DIR:$target_sdk_dir:") $l
done

# find out all perl scripts in $native_sysroot and modify them replacing the
# host perl with SDK perl.
for perl_script in $($SUDO_EXEC grep "^#!.*perl" -rl $native_sysroot); do
	$SUDO_EXEC sed -i -e "s:^#! */usr/bin/perl.*:#! /usr/bin/env perl:g" -e \
		"s: /usr/bin/perl: /usr/bin/env perl:g" $perl_script
done

echo done

# delete the relocating script, so that user is forced to re-run the installer
# if he/she wants another location for the sdk
if [ $savescripts = 0 ] ; then
	$SUDO_EXEC rm ${env_setup_script%/*}/relocate_sdk.py ${env_setup_script%/*}/relocate_sdk.sh
fi

echo "SDK has been successfully set up and is ready to be used."

exit 0

MARKER:
EOF
	# add execution permission
	chmod +x ${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.sh

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
do_populate_sdk[depends] += "${@' '.join([x + ':do_populate_sysroot' for x in d.getVar('SDK_DEPENDS', True).split()])}"
do_populate_sdk[rdepends] = "${@' '.join([x + ':do_populate_sysroot' for x in d.getVar('SDK_RDEPENDS', True).split()])}"
do_populate_sdk[recrdeptask] += "do_packagedata do_package_write_rpm do_package_write_ipk do_package_write_deb"
addtask populate_sdk
