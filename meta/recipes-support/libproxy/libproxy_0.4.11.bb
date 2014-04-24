SUMMARY = "Library providing automatic proxy configuration management"
HOMEPAGE = "http://code.google.com/p/libproxy/"
BUGTRACKER = "http://code.google.com/p/libproxy/issues/list"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://utils/proxy.c;beginline=1;endline=18;md5=55152a1006d7dafbef32baf9c30a99c0"

DEPENDS = "gconf glib-2.0"

SRC_URI = "http://libproxy.googlecode.com/files/libproxy-${PV}.zip \
          "

SRC_URI[md5sum] = "06be230b8b612e64997f8f15af6373aa"
SRC_URI[sha256sum] = "3175abcb49c0b37fd82c697927783bf7d53de3884a8559bc594e59caa09bdb04"

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
    if bb.utils.contains("INCOMPATIBLE_LICENSE", "GPLv3", "x", "", d) == "x" or bb.utils.contains("DISTRO_FEATURES", "x11", "x", "", d) == "":
        d.setVar("EXTRA_OECMAKE", d.getVar("EXTRA_OECMAKE").replace("-DWITH_GNOME=yes", "-DWITH_GNOME=no"))
        d.setVar("DEPENDS", " ".join(i for i in d.getVar("DEPENDS").split() if i != "gconf"))
}
