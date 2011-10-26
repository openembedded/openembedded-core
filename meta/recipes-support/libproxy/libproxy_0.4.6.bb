DESCRIPTION = "A library that provides automatic proxy configuration management"
HOMEPAGE = "http://code.google.com/p/libproxy/"
BUGTRACKER = "http://code.google.com/p/libproxy/issues/list"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7d7044444a7b1b116e8783edcdb44ff4 \
                    file://utils/proxy.c;beginline=1;endline=18;md5=55152a1006d7dafbef32baf9c30a99c0"

DEPENDS = "gconf"

PR = "r1"

SRC_URI = "http://libproxy.googlecode.com/files/libproxy-${PV}.tar.gz"

SRC_URI[md5sum] = "199c6b120baf1f7258a55f38d5ec74f5"
SRC_URI[sha256sum] = "9ad912e63b1efca98fb442240a2bc7302e6021c1d0b1b9363327729f29462f30"

PR = "r2"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DWITH_WEBKIT=no -DWITH_GNOME=yes -DWITH_KDE4=no \
	      -DWITH_PYTHON=no -DWITH_PERL=no -DWITH_MOZJS=no -DWITH_NM=no -DLIB_INSTALL_DIR=${libdir}"

FILES_${PN}-dev += "${datadir}/cmake"
FILES_${PN}-dbg += "${libdir}/libproxy/${PV}/plugins/.debug/ ${libdir}/libproxy/${PV}/modules/.debug/"

do_configure_prepend() {
	export HOST_SYS=${HOST_SYS}
	export BUILD_SYS=${BUILD_SYS}
}
