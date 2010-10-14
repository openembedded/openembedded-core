DESCRIPTION = "Tools for GUPnP"
LICENSE = "GPL"
DEPENDS = "gupnp gtk+ libglade gnome-icon-theme"

SRC_URI = "http://gupnp.org/sites/all/files/sources/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig
