DESCRIPTION = "Python bindings for DBus, a socket-based message bus system for interprocess communication"
SECTION = "devel/python"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f5612614133e9a2f2dad527d97554670"
DEPENDS = "expat dbus dbus-glib virtual/libintl python-pyrex-native"
PR = "r1"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus-python/dbus-python-${PV}.tar.gz"

SRC_URI[md5sum] = "775a8235736bf760cdd96e2d76546469"
SRC_URI[sha256sum] = "9e46f97d739dde8a5ab33ec6b11da58794c4c20804aacdad2880b7d3bc05187a"
S = "${WORKDIR}/dbus-python-${PV}"

inherit distutils-base autotools pkgconfig

export BUILD_SYS
export HOST_SYS

export STAGING_LIBDIR
export STAGING_INCDIR

RDEPENDS_${PN} = "python-io python-logging python-stringold python-threading python-xml"

FILES_${PN}-dev += "${libdir}/pkgconfig"
