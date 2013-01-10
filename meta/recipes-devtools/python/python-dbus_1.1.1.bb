DESCRIPTION = "Python bindings for DBus, a socket-based message bus system for interprocess communication"
SECTION = "devel/python"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f5612614133e9a2f2dad527d97554670"
DEPENDS = "expat dbus dbus-glib virtual/libintl python-pyrex-native"
PR = "r2"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus-python/dbus-python-${PV}.tar.gz \
           file://obsolete_automake_macros.patch \
"

SRC_URI[md5sum] = "742c7432ad0f7c3f98291d58fa2e35dc"
SRC_URI[sha256sum] = "8917ca4fb8f4d693aee18d200cbad166b2c938dfb88c03bb4ab3d90a7c915e88"
S = "${WORKDIR}/dbus-python-${PV}"

inherit distutils-base autotools pkgconfig

export BUILD_SYS
export HOST_SYS

export STAGING_LIBDIR
export STAGING_INCDIR

RDEPENDS_${PN} = "python-io python-logging python-stringold python-threading python-xml"

FILES_${PN}-dev += "${libdir}/pkgconfig"
