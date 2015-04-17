SECTION = "devel"
SUMMARY = "GNU multiprecision arithmetic library"
DESCRIPTION = "GMP is a free library for arbitrary precision arithmetic, operating on signed integers, rational numbers, and floating point numbers"
HOMEPAGE = "http://gmplib.org/"
LICENSE = "GPLv3 LGPLv3"

REVISION ?= ""

SRC_URI = "https://gmplib.org/download/${BPN}/${BP}${REVISION}.tar.bz2 \
	   file://configure.patch \
	   file://amd64.patch "

inherit autotools texinfo

ARM_INSTRUCTION_SET = "arm"

acpaths = ""

BBCLASSEXTEND = "native nativesdk"

PACKAGECONFIG ??= ""
PACKAGECONFIG[readline] = "--with-readline=yes,--with-readline=no,readline"

EXTRA_OECONF += " --enable-cxx=detect"

PACKAGES =+ "libgmpxx"
FILES_libgmpxx = "${libdir}/libgmpxx${SOLIBS}"

do_install_append_class-target() {
	sed -i "s|--sysroot=${STAGING_DIR_HOST}||g" ${D}${includedir}/gmp.h
}

SSTATE_SCAN_FILES += "gmp.h"
