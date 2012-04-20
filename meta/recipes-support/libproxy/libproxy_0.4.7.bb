DESCRIPTION = "A library that provides automatic proxy configuration management"
HOMEPAGE = "http://code.google.com/p/libproxy/"
BUGTRACKER = "http://code.google.com/p/libproxy/issues/list"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7d7044444a7b1b116e8783edcdb44ff4 \
                    file://utils/proxy.c;beginline=1;endline=18;md5=55152a1006d7dafbef32baf9c30a99c0"

DEPENDS = "gconf"

PR = "r4"

SRC_URI = "http://libproxy.googlecode.com/files/libproxy-${PV}.tar.gz \
           file://g++-namepace.patch \
           file://libproxy_fix_for_gcc4.7.patch \
          "

SRC_URI[md5sum] = "509e03a488a61cd62bfbaf3ab6a2a7a5"
SRC_URI[sha256sum] = "8fe0a58810139ba3c2e186deccf3e68adcd127aa0e972b0862b30b3dde493797"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DWITH_WEBKIT=no -DWITH_GNOME=yes -DWITH_KDE4=no \
	      -DWITH_PYTHON=no -DWITH_PERL=no -DWITH_MOZJS=no -DWITH_NM=no -DLIB_INSTALL_DIR=${libdir}"

FILES_${PN}-dev += "${datadir}/cmake"
FILES_${PN}-dbg += "${libdir}/libproxy/${PV}/plugins/.debug/ ${libdir}/libproxy/${PV}/modules/.debug/"

do_configure_prepend() {
	export HOST_SYS=${HOST_SYS}
	export BUILD_SYS=${BUILD_SYS}
}

python() {
    if base_contains("INCOMPATIBLE_LICENSE", "GPLv3", "x", "", d) == "x" or base_contains("DISTRO_FEATURES", "x11", "x", "", d) == "":
        d.setVar("EXTRA_OECMAKE", d.getVar("EXTRA_OECMAKE").replace("-DWITH_GNOME=yes", "-DWITH_GNOME=no"))
        d.setVar("DEPENDS", " ".join(i for i in d.getVar("DEPENDS").split() if i != "gconf"))
}
