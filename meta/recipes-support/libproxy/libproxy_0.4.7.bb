DESCRIPTION = "A library that provides automatic proxy configuration management"
HOMEPAGE = "http://code.google.com/p/libproxy/"
BUGTRACKER = "http://code.google.com/p/libproxy/issues/list"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7d7044444a7b1b116e8783edcdb44ff4 \
                    file://utils/proxy.c;beginline=1;endline=18;md5=55152a1006d7dafbef32baf9c30a99c0"

DEPENDS = "gconf glib-2.0"

PR = "r6"

SRC_URI = "http://libproxy.googlecode.com/files/libproxy-${PV}.zip \
           file://g++-namepace.patch \
           file://libproxy_fix_for_gcc4.7.patch \
           file://libproxy-0.4.7-CVE-2012-4504.patch \
          "

SRC_URI[md5sum] = "62585570de17d10f03a5a63b701ffd52"
SRC_URI[sha256sum] = "429a19c57d9990349d622beecc805b23051caa62a478066bff5d9a312a8937be"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DWITH_WEBKIT=no -DWITH_GNOME=yes -DWITH_KDE4=no \
	      -DWITH_PYTHON=no -DWITH_PERL=no -DWITH_MOZJS=no -DWITH_NM=no -DLIB_INSTALL_DIR=${libdir} -DLIBEXEC_INSTALL_DIR=${libexecdir}"

FILES_${PN} += "${libdir}/${BPN}/${PV}/modules"
FILES_${PN}-dev += "${datadir}/cmake"
FILES_${PN}-dbg += "${libdir}/${BPN}/${PV}/plugins/.debug/ ${libdir}/${BPN}/${PV}/modules/.debug/"

do_configure_prepend() {
	export HOST_SYS=${HOST_SYS}
	export BUILD_SYS=${BUILD_SYS}
}

python() {
    if base_contains("INCOMPATIBLE_LICENSE", "GPLv3", "x", "", d) == "x" or base_contains("DISTRO_FEATURES", "x11", "x", "", d) == "":
        d.setVar("EXTRA_OECMAKE", d.getVar("EXTRA_OECMAKE").replace("-DWITH_GNOME=yes", "-DWITH_GNOME=no"))
        d.setVar("DEPENDS", " ".join(i for i in d.getVar("DEPENDS").split() if i != "gconf"))
}
