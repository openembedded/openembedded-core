DESCRIPTION = "Libksba provides an easy API to create and parse X.509 and CMS related objects."
HOMEPAGE = "http://www.gnupg.org/related_software/libksba/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949"

DEPENDS = "libgpg-error"

inherit autotools binconfig

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "e797f370b69b4dc776499d6a071ae137"
SRC_URI[sha256sum] = "09afce65b03d027cbec10d21464f4f651cdfd269e38b404f83e48d3e2a3c934b"
