SRC_URI = "http://www.opensync.org/download/releases/${PV}/msynctool-${PV}.tar.bz2"

LICENSE = "GPL"
DEPENDS = "libopensync glib-2.0"
HOMEPAGE = "http://www.opensync.org/"

inherit cmake pkgconfig
