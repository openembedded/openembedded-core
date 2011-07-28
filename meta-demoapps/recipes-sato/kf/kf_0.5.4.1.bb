DESCRIPTION = "Kf is a GTK+ instant messaging client."
LICENSE = "GPL"
DEPENDS = "libxml2 glib-2.0 gtk+ loudmouth libglade"
PR = "r4"

SRC_URI = "http://jabberstudio.2nw.net/${BPN}/${BPN}-${PV}.tar.gz \
           file://fix-configure.patch;patch=1 \
           file://fix-desktop-file.patch;patch=0 \
           file://gcc4.patch;patch=1"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-binreloc"

export PKG_CONFIG="${STAGING_BINDIR_NATIVE}/pkg-config"

