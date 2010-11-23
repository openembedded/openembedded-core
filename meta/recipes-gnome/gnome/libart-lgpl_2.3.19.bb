DESCRIPTION = "Library of functions for 2D graphics"
SECTION = "x11/gnome"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"
PR = "r1"

ART_CONFIG = "${HOST_ARCH}/art_config.h"

# can't use gnome.oeclass due to _ in filename
SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libart_lgpl/2.3/libart_lgpl-${PV}.tar.bz2 \
       file://${ART_CONFIG} \
       file://Makefile.am.patch;patch=1"

inherit autotools pkgconfig

DEPENDS = ""

FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dev += "${bindir}/libart2-config"

S = "${WORKDIR}/libart_lgpl-${PV}"

do_configure_prepend() {
	cp ${WORKDIR}/${ART_CONFIG} ${S}/art_config.h
}

EXTRA_OECONF = "--disable-gtk-doc"

