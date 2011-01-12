DESCRIPTION = "Packge of environment files for SDK"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
LICENSE = "MIT"
PR = "r1"

EXCLUDE_FROM_WORLD = "1"

inherit toolchain-scripts
# get target config site before inheritting corss-canadian
TARGET_CONFIG_SITE := "${@siteinfo_get_files(d)}"

SDK_DIR = "${WORKDIR}/sdk"
SDK_OUTPUT = "${SDK_DIR}/image"
SDKTARGETSYSROOT = "${SDKPATH}/sysroots/${TARGET_SYS}"

inherit cross-canadian

do_generate_content[nostamp] = "1"
do_generate_content() {

    rm -rf ${SDK_OUTPUT}
    mkdir -p ${SDK_OUTPUT}/${SDKPATH}

    siteconfig=${SDK_OUTPUT}/${SDKPATH}/site-config-${OLD_MULTIMACH_TARGET_SYS}

    touch $siteconfig
    for sitefile in ${TARGET_CONFIG_SITE} ; do
        cat $sitefile >> $siteconfig
    done

    toolchain_create_sdk_env_script_for_installer

    # Add version information
    versionfile=${SDK_OUTPUT}/${SDKPATH}/version-${OLD_MULTIMACH_TARGET_SYS}
    touch $versionfile
    echo 'Distro: ${DISTRO}' >> $versionfile
    echo 'Distro Version: ${DISTRO_VERSION}' >> $versionfile
    echo 'Metadata Revision: ${METADATA_REVISION}' >> $versionfile
    echo 'Timestamp: ${DATETIME}' >> $versionfile
}
do_generate_content[recrdeptask] = "do_compile"
addtask generate_content before do_install after do_compile

do_install[nostamp] = "1"
do_install() {
    install -d ${D}/${SDKPATH}
    install -m 0644 -t ${D}/${SDKPATH} ${SDK_OUTPUT}/${SDKPATH}/*
}

PN = "meta-environment-${TRANSLATED_TARGET_ARCH}"
PACKAGES = "${PN}"
FILES_${PN}= " \
    ${SDKPATH}/* \
    "

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_populage_sysroot[noexec] = "1"
