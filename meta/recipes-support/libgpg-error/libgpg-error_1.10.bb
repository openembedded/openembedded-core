DESCRIPTION = "a small library that defines common error values for all GnuPG components"
HOMEPAGE = "http://www.gnupg.org/related_software/libgpg-error/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/gpg-error.h;endline=23;md5=83c16c8f5cea85affa1ff270a6f4fcff \
                    file://src/init.c;endline=20;md5=b69742f2a8827d494c6f6a4b1768416c"


SECTION = "libs"
PR = "r1"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgpg-error/libgpg-error-${PV}.tar.bz2 \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "736a03daa9dc5873047d4eb4a9c22a16"
SRC_URI[sha256sum] = "520629b4568b5c29b1991c8ffc267c8bdee5f223c7333c42a651b56f9b1c5431"

# move libgpg-error-config into -dev package
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir}/*"

inherit autotools binconfig pkgconfig gettext

do_install_append() {
	# we don't have common lisp in OE
	rm -rf "${D}${datadir}/common-lisp/"
}

BBCLASSEXTEND = "native"
