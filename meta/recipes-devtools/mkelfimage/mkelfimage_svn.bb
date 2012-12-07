DESCRIPTION = "A utility to create ELF boot images from Linux kernel images"
HOMEPAGE = "http://www.coreboot.org/Mkelfimage"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea5bed2f60d357618ca161ad539f7c0a"

SRCREV = "6637"
PV = "1.0.0+svn${SRCPV}"
PR = "r2"

DEPENDS += "zlib"

SRC_URI = "svn://coreboot.org/coreboot/trunk/util;module=mkelfImage;protocol=svn \
           file://cross-compile.patch \
           file://fix-makefile-to-find-libz.patch \
          "

S = "${WORKDIR}/mkelfImage"

CFLAGS += "-fno-stack-protector"
CACHED_CONFIGUREVARS += "HOST_CC='${BUILD_CC}'"
EXTRA_OEMAKE += "HOST_CPPFLAGS='${BUILD_CPPFLAGS}'"

inherit autotools

do_install_append() {
	rmdir ${D}${datadir}/mkelfImage/elf32-i386
	rmdir ${D}${datadir}/mkelfImage
}

BBCLASSEXTEND = "native"
