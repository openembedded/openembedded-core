HOMEPAGE = "http://freedesktop.org/wiki/Software/XKeyboardConfig"
DESCRIPTION = "Keyboard configuration database for X Window"
SRC_URI = "http://xlibs.freedesktop.org/xkbdesc/xkeyboard-config-${PV}.tar.bz2"
SECTION = "x11/libs"
LICENSE = "MIT-X"
DEPENDS = "intltool xkbcomp-native glib-2.0"
PR = "r2"

EXTRA_OECONF = "--with-xkb-rules-symlink=xorg"

RDEPENDS_${PN} += "xkbcomp"
FILES_${PN} += "${datadir}/X11/xkb"

inherit autotools pkgconfig

do_install_append () {
    install -d ${D}/usr/share/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}
