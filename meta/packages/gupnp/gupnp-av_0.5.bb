LICENSE = "LGPL"
DEPENDS = "gupnp"

SRC_URI = "http://gupnp.org/sources/${PN}/${PN}-${PV}.tar.gz"

inherit autotools_stage pkgconfig
