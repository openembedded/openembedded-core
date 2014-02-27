SUMMARY = "Utility for creating ELF boot images for ELF-based Linux kernel images"
HOMEPAGE = "http://www.coreboot.org/Mkelfimage"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea5bed2f60d357618ca161ad539f7c0a"

SRCREV = "686a48a339b3200184c27e7f98d4c03180b2be6c"
PV = "4.0+git${SRCPV}"

DEPENDS += "zlib"

SRC_URI = "git://review.coreboot.org/p/coreboot;protocol=http \
           file://cross-compile.patch   \
           file://fix-makefile-to-find-libz.patch   \
          "

S = "${WORKDIR}/git/util/mkelfImage"

CFLAGS += "-fno-stack-protector"
CACHED_CONFIGUREVARS += "HOST_CC='${BUILD_CC}'"
EXTRA_OEMAKE += "HOST_CPPFLAGS='${BUILD_CPPFLAGS}'"

inherit autotools-brokensep

do_install_append() {
	rmdir ${D}${datadir}/mkelfImage/elf32-i386
	rmdir ${D}${datadir}/mkelfImage
}

BBCLASSEXTEND = "native"
