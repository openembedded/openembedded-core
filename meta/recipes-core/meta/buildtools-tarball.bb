DESCRIPTION = "SDK type target for building a standalone tarball containing python, chrpath, git and tar. The \
               tarball can be used to run bitbake builds on systems which don't meet the usual version requirements."
SUMMARY = "Standalone tarball for running builds on systems with inadequate software"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

TOOLCHAIN_TARGET_TASK ?= ""

TOOLCHAIN_HOST_TASK ?= "\
    nativesdk-python-core \
    nativesdk-python-textutils \
    nativesdk-python-sqlite3 \
    nativesdk-python-pickle \
    nativesdk-python-logging \
    nativesdk-python-elementtree \
    nativesdk-python-curses \
    nativesdk-python-compile \
    nativesdk-python-compiler \
    nativesdk-python-fcntl \
    nativesdk-python-shell \
    nativesdk-python-misc \
    nativesdk-python-multiprocessing \
    nativesdk-python-subprocess \
    nativesdk-python-xmlrpc \
    nativesdk-python-netclient \
    nativesdk-python-netserver \
    nativesdk-python-distutils \
    nativesdk-python-unixadmin \
    nativesdk-python-compression \
    nativesdk-python-json \
    nativesdk-python-unittest \
    nativesdk-chrpath \
    nativesdk-tar \
    nativesdk-git \
    "

TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-buildtools-nativesdk-standalone-${DISTRO_VERSION}"

RDEPENDS = "${TOOLCHAIN_HOST_TASK}"

EXCLUDE_FROM_WORLD = "1"

inherit meta
inherit populate_sdk

create_sdk_files_append () {
	rm -f ${SDK_OUTPUT}/${SDKPATH}/site-config*
	
	cat ${SDK_OUTPUT}/${SDKPATH}/environment-setup* | grep " PATH=\|OECORE_NATIVE_SYSROOT" > ${WORKDIR}/envtmp
	mv ${WORKDIR}/envtmp ${SDK_OUTPUT}/${SDKPATH}/environment-setup*
}
