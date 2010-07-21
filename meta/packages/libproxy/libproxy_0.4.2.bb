DESCRIPTION = "A library that provides automatic proxy configuration management"
HOMEPAGE = "http://code.google.com/p/libproxy/"
BUGTRACKER = "http://code.google.com/p/libproxy/issues/list"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7d7044444a7b1b116e8783edcdb44ff4 \
                    file://utils/proxy.c;beginline=1;endline=18;md5=55152a1006d7dafbef32baf9c30a99c0"


DEPENDS = "virtual/libx11 xmu gconf-dbus"

SRC_URI = "http://libproxy.googlecode.com/files/libproxy-${PV}.tar.gz"

PR = "r1"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DWITH_WEBKIT=no -DWITH_GNOME=yes -DWITH_KDE4=no \
	      -DWITH_PYTHON=no -DWITH_MOZJS=no -DWITH_NM=no"

FILES_${PN}-dbg += "${libdir}/libproxy/0.4.2/plugins/"

do_configure_prepend() {
	export HOST_SYS=${HOST_SYS}
	export BUILD_SYS=${BUILD_SYS}
}
