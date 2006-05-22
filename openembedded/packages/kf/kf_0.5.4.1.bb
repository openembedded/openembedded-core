LICENSE = "GPL"
DEPENDS = "libxml2 glib-2.0 gtk+ loudmouth"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Kf is a GTK+ instant messaging client."
PR = "r2"

SRC_URI = "http://jabberstudio.2nw.net/${PN}/${PN}-${PV}.tar.gz \
           file://fix-configure.patch;patch=1 \
           file://fix-desktop-file.patch;patch=0"

inherit autotools pkgconfig

export PKG_CONFIG="${STAGING_BINDIR}/pkg-config"

EXTRA_OECONF = "--disable-binreloc"

