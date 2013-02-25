DESCRIPTION = "IPC library used by GnuPG and GPGME"
HOMEPAGE = "http://www.gnupg.org/related_software/libassuan/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv3 & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/assuan.c;endline=20;md5=0f465544183405055ec179869fc5b5ba \
                    file://src/assuan-defs.h;endline=20;md5=20cd55535260ca1779edae5c7b80b21e"

DEPENDS = "libgpg-error"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libassuan/libassuan-${PV}.tar.bz2 \
	   file://libassuan-add-pkgconfig-support.patch"

SRC_URI[md5sum] = "b3231eec8e567f4f9294474a387378f5"
SRC_URI[sha256sum] = "a4c5bd3a0c2726e8bcc2ea8a3b15f7aae3bd92d4f85968f315fd3a75d101c267"

inherit autotools binconfig pkgconfig
