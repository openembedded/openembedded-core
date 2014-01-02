SUMMARY = "Package of environment files for SDK"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
LICENSE = "MIT"
PR = "r8"

EXCLUDE_FROM_WORLD = "1"

inherit toolchain-scripts
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
REAL_MULTIMACH_TARGET_SYS = "${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDKTARGETSYSROOT = "${SDKPATH}/sysroots/${TARGET_SYS}"

inherit cross-canadian

do_generate_content[nostamp] = "1"
do_generate_content() {

    rm -rf ${SDK_OUTPUT}
    mkdir -p ${SDK_OUTPUT}/${SDKPATH}

    toolchain_create_sdk_siteconfig ${SDK_OUTPUT}/${SDKPATH}/site-config-${REAL_MULTIMACH_TARGET_SYS}

    toolchain_create_sdk_env_script ${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS} ${REAL_MULTIMACH_TARGET_SYS} '##SDKTARGETSYSROOT##'  ${target_libdir} 

    # Add version information
    toolchain_create_sdk_version ${SDK_OUTPUT}/${SDKPATH}/version-${REAL_MULTIMACH_TARGET_SYS}
}
addtask generate_content before do_install after do_compile

do_install[nostamp] = "1"
do_install() {
    install -d ${D}/${SDKPATH}
    install -m 0644 -t ${D}/${SDKPATH} ${SDK_OUTPUT}/${SDKPATH}/*
}

PN = "meta-environment-${MACHINE}"
PACKAGES = "${PN}"
FILES_${PN}= " \
    ${SDKPATH}/* \
    "

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_populate_sysroot[noexec] = "1"
