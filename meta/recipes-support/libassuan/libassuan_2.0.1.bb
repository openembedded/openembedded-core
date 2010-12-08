DESCRIPTION = "IPC library used by GnuPG and GPGME"
HOMEPAGE = "http://www.gnupg.org/related_software/libassuan/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv3 & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/assuan.c;endline=20;md5=fbd7f2a44aab170430d59380f94f85f8 \
                    file://src/assuan-defs.h;endline=20;md5=20cd55535260ca1779edae5c7b80b21e"

DEPENDS = "libgpg-error"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libassuan/libassuan-${PV}.tar.bz2"

SRC_URI[md5sum] = "53a7d4c22af909d7236d17d454ef935b"
SRC_URI[sha256sum] = "1117be532ac41b13790b4756df7fa747d3d04264f7b4544aa9d7ff468a24b457"

inherit autotools binconfig
