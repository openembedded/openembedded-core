SUMMARY = "The shared library for the S-Lang extension language"

DESCRIPTION = "S-Lang is an interpreted language and a programming library.  The \
S-Lang language was designed so that it can be easily embedded into \
a program to provide the program with a powerful extension language. \
The S-Lang library, provided in this package, provides the S-Lang \
extension language.  S-Lang's syntax resembles C, which makes it easy \
to recode S-Lang procedures in C if you need to."

HOMEPAGE = "http://www.jedsoft.org/slang/"
SECTION = "libs"
DEPENDS = "pcre"
PR = "r6"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=a52a18a472d4f7e45479b06563717c02"


SRC_URI = "ftp://space.mit.edu/pub/davis/slang/v2.2/slang-${PV}.tar.bz2 \
          "

inherit autotools

SRC_URI[md5sum] = "7fcfd447e378f07dd0c0bae671fe6487"
SRC_URI[sha256sum] = "9a8257a9a2a55099af858b13338dc8f3a06dd2069f46f0df2c9c3bb84a01d5db"

EXTRA_OECONF += " --without-z --without-png --x-includes=${STAGING_DIR_HOST}/usr/include/X11 --x-libraries=${STAGING_DIR_HOST}/usr/lib"

do_install() {
	oe_runmake install DESTDIR=${D} -e 'INST_LIB_DIR=${STAGING_DIR_HOST}/usr/lib'
}

FILES_${PN} += "${datadir}/slsh/"

PARALLEL_MAKE = ""
