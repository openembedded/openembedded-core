LICENSE = "LGPL"
DEPENDS = "libopensync eds-dbus"
SRC_URI = "http://www.o-hand.com/~chris/${PN}-${PV}.tar.gz \
	   file://fix-warnings.patch;patch=1"

inherit autotools pkgconfig

