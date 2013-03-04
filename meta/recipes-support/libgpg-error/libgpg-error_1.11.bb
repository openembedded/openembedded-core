DESCRIPTION = "a small library that defines common error values for all GnuPG components"
HOMEPAGE = "http://www.gnupg.org/related_software/libgpg-error/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/gpg-error.h.in;endline=23;md5=6ac0378874589a44d53512b3786b4bc0 \
                    file://src/init.c;endline=20;md5=b69742f2a8827d494c6f6a4b1768416c"


SECTION = "libs"
PR = "r0"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgpg-error/libgpg-error-${PV}.tar.bz2 \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "b9fa55b71cae73cb2e44254c2acc4e2c"
SRC_URI[sha256sum] = "ae3376480a073b120c7add08f1e6cfcc08500648ccf22461cd42db6224a218c0"

inherit autotools binconfig pkgconfig gettext

FILES_${PN}-dev += "${bindir}/gpg-error"

do_install_append() {
	# we don't have common lisp in OE
	rm -rf "${D}${datadir}/common-lisp/"
}

BBCLASSEXTEND = "native"
