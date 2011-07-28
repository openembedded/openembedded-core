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

SRC_URI[md5sum] = "3e7d00fd2ee8a0b9c51ac7616ef3f1ec"
SRC_URI[sha256sum] = "61e0cba33dcbade2dce953b95f06b843af2a73de875303f2585227ecd475b4d8"

inherit autotools binconfig
