LICENSE = "GPL"
DEPENDS = "gupnp gtk+ libglade gnome-icon-theme"

SRC_URI = "http://gupnp.org/sources/${PN}/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig
