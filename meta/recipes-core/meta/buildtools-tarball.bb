DESCRIPTION = "SDK type target for building a standalone tarball containing python, chrpath, make, git and tar. The \
               tarball can be used to run bitbake builds on systems which don't meet the usual version requirements."
SUMMARY = "Standalone tarball for running builds on systems with inadequate software"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
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
    nativesdk-python-mmap \
    nativesdk-python-difflib \
    nativesdk-python-pprint \
    nativesdk-python-git \
    nativesdk-python-pkgutil \
    nativesdk-ncurses-terminfo-base \
    nativesdk-chrpath \
    nativesdk-tar \
    nativesdk-git \
    nativesdk-pigz \
    nativesdk-make \
    "

TOOLCHAIN_OUTPUTNAME ?= "${SDK_NAME}-buildtools-nativesdk-standalone-${DISTRO_VERSION}"

RDEPENDS = "${TOOLCHAIN_HOST_TASK}"

EXCLUDE_FROM_WORLD = "1"

inherit meta
inherit populate_sdk

create_sdk_files_append () {
	rm -f ${SDK_OUTPUT}/${SDKPATH}/site-config-*
	rm -f ${SDK_OUTPUT}/${SDKPATH}/environment-setup-*
	rm -f ${SDK_OUTPUT}/${SDKPATH}/version-*

	# Generate new (mini) sdk-environment-setup file
	script=${1:-${SDK_OUTPUT}/${SDKPATH}/environment-setup-${SDK_SYS}}
	touch $script
	echo 'export PATH=${SDKPATHNATIVE}${bindir_nativesdk}:$PATH' >> $script
	# In order for the self-extraction script to correctly extract and set up things,
	# we need a 'OECORE_NATIVE_SYSROOT=xxx' line in environment setup script.
	# However, buildtools-tarball is inherently a tool set instead of a fully functional SDK,
	# so instead of exporting the variable, we use a comment here.
	echo '#OECORE_NATIVE_SYSROOT="${SDKPATHNATIVE}"' >> $script
	toolchain_create_sdk_version ${SDK_OUTPUT}/${SDKPATH}/version-${SDK_SYS}
}
