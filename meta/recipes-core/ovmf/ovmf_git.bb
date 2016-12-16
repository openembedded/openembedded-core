DESCRIPTION = "OVMF - UEFI firmware for Qemu and KVM"
HOMEPAGE = "http://sourceforge.net/apps/mediawiki/tianocore/index.php?title=OVMF"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://OvmfPkg/License.txt;md5=343dc88e82ff33d042074f62050c3496"

SRC_URI = "git://github.com/tianocore/edk2.git;branch=master \
	file://0001-BaseTools-Force-tools-variables-to-host-toolchain.patch \
	file://0001-OvmfPkg-Enable-BGRT-in-OVMF.patch \
	file://0002-ovmf-update-path-to-native-BaseTools.patch \
	file://0003-BaseTools-makefile-adjust-to-build-in-under-bitbake.patch \
        "

SRCREV="4575a602ca6072ee9d04150b38bfb143cbff8588"

PARALLEL_MAKE = ""

S = "${WORKDIR}/git"

DEPENDS_class-native="util-linux-native iasl-native ossp-uuid-native"

DEPENDS_class-target="ovmf-native"

DEPENDS_append = " nasm-native"

EDK_TOOLS_DIR="edk2_basetools"

# OVMF has trouble building with the default optimization of -O2.
BUILD_OPTIMIZATION="-pipe"

# OVMF supports IA only, although it could conceivably support ARM someday.
COMPATIBLE_HOST='(i.86|x86_64).*'

do_patch_append_class-native() {
    bb.build.exec_func('do_fix_iasl', d)
    bb.build.exec_func('do_fix_toolchain', d)
}

do_fix_basetools_location() {
    sed -i -e 's#BBAKE_EDK_TOOLS_PATH#${STAGING_BINDIR_NATIVE}/${EDK_TOOLS_DIR}#' ${S}/OvmfPkg/build.sh
}

do_patch_append_class-target() {
    bb.build.exec_func('do_fix_basetools_location', d)
}


do_fix_iasl() {
    sed -i -e 's#/usr/bin/iasl#${STAGING_BINDIR_NATIVE}/iasl#' ${S}/BaseTools/Conf/tools_def.template
}

do_fix_toolchain(){
    sed -i -e 's#DEF(ELFGCC_BIN)/#${TARGET_PREFIX}#' ${S}/BaseTools/Conf/tools_def.template
    sed -i -e 's#DEF(GCC.*PREFIX)#${TARGET_PREFIX}#' ${S}/BaseTools/Conf/tools_def.template
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nLFLAGS += ${BUILD_LDFLAGS}#" ${S}/BaseTools/Source/C/Makefiles/app.makefile
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nCFLAGS += ${BUILD_CFLAGS}#" ${S}/BaseTools/Source/C/Makefiles/app.makefile
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nLFLAGS += ${BUILD_LDFLAGS}#" ${S}/BaseTools/Source/C/VfrCompile/GNUmakefile
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nCFLAGS += ${BUILD_CFLAGS}#" ${S}/BaseTools/Source/C/VfrCompile/GNUmakefile
}

GCC_VER="$(${CC} -v 2>&1 | tail -n1 | awk '{print $3}')"

fixup_target_tools() {
    case ${1} in
      4.4.*)
        FIXED_GCCVER=GCC44
        ;;
      4.5.*)
        FIXED_GCCVER=GCC45
        ;;
      4.6.*)
        FIXED_GCCVER=GCC46
        ;;
      4.7.*)
        FIXED_GCCVER=GCC47
        ;;
      4.8.*)
        FIXED_GCCVER=GCC48
        ;;
      4.9.*)
        FIXED_GCCVER=GCC49
        ;;
      *)
        FIXED_GCCVER=GCC5
        ;;
    esac
    echo ${FIXED_GCCVER}
}

do_compile_class-native() {
    oe_runmake -C ${S}/BaseTools
}

do_compile_class-target() {
    export LFLAGS="${LDFLAGS}"
    OVMF_ARCH="X64"
    if [ "${TARGET_ARCH}" != "x86_64" ] ; then
        OVMF_ARCH="IA32"
    fi

    FIXED_GCCVER=$(fixup_target_tools ${GCC_VER})
    echo FIXED_GCCVER is ${FIXED_GCCVER}
    ${S}/OvmfPkg/build.sh -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER}
}

do_install_class-native() {
    install -d ${D}/${bindir}/edk2_basetools
    cp -r ${S}/BaseTools ${D}/${bindir}/${EDK_TOOLS_DIR}
}

do_install_class-target() {
    OVMF_DIR_SUFFIX="X64"
    if [ "${TARGET_ARCH}" != "x86_64" ] ; then
        OVMF_DIR_SUFFIX="Ia32" # Note the different capitalization
    fi
    install -d ${D}${datadir}/ovmf

    FIXED_GCCVER=$(fixup_target_tools ${GCC_VER})
    build_dir="${S}/Build/Ovmf$OVMF_DIR_SUFFIX/RELEASE_${FIXED_GCCVER}"
    install -m 0755 ${build_dir}/FV/OVMF.fd \
	${D}${datadir}/ovmf/bios.bin
}

BBCLASSEXTEND = "native"
