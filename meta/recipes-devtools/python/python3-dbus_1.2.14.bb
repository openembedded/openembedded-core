SUMMARY = "Python bindings for the DBus inter-process communication system"
SECTION = "devel/python"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=b03240518994df6d8c974675675e5ca4"
DEPENDS = "expat dbus glib-2.0 virtual/libintl"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus-python/dbus-python-${PV}.tar.gz"

SRC_URI[md5sum] = "c8739234fca9fba26368d1a337abe830"
SRC_URI[sha256sum] = "b10206ba3dd641e4e46411ab91471c88e0eec1749860e4285193ee68df84ac31"

S = "${WORKDIR}/dbus-python-${PV}"

inherit distutils3-base autotools pkgconfig

# documentation needs python3-sphinx, which is not in oe-core or meta-python for now
# change to use PACKAGECONFIG when python3-sphinx is added to oe-core
EXTRA_OECONF += "--disable-documentation"


RDEPENDS_${PN} = "python3-io python3-logging python3-stringold python3-threading python3-xml"

FILES_${PN}-dev += "${libdir}/pkgconfig"

BBCLASSEXTEND = "native nativesdk"
