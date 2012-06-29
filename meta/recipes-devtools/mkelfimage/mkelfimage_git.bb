DESCRIPTION = "A utility to create ELF boot images from Linux kernel images"
HOMEPAGE = "http://www.coreboot.org/Mkelfimage"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea5bed2f60d357618ca161ad539f7c0a"

SRCREV = "e13632a939e7dde8d7d44fc73e73a41dae8060c3"
PV = "1.0.0+gitr${SRCPV}"
PR = "r1"

SRC_URI = "git://review.coreboot.org/p/coreboot;protocol=http;branch=master"

S = "${WORKDIR}/git/util/mkelfImage"

inherit autotools

BBCLASSEXTEND = "native"
