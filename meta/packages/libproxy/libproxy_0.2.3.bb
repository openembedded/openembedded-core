DESCRIPTION = "A library that provides automatic proxy configuration management"
HOMEPAGE = "http://code.google.com/p/libproxy/"
LICENSE = "LGPL"
SECTION = "libs"

DEPENDS = "virtual/libx11 xmu gconf-dbus"

SRC_URI = "http://libproxy.googlecode.com/files/libproxy-${PV}.tar.gz \
           file://asneededfix.patch;patch=1"
S = "${WORKDIR}/libproxy-${PV}"

inherit autotools_stage pkgconfig

EXTRA_OECONF = "--without-kde --with-gnome --without-webkit --without-python --without-mozjs --without-networkmanager"

FILES_${PN}-dbg += "${libdir}/libproxy/0.2.3/plugins/"