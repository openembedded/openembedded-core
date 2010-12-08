DESCRIPTION = "Keyboard configuration database for X Window"
HOMEPAGE = "http://freedesktop.org/wiki/Software/XKeyboardConfig"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=xkeyboard-config"

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=0e7f21ca7db975c63467d2e7624a12f9"

SRC_URI = "http://people.freedesktop.org/~svu/xkeyboard-config-${PV}.tar.bz2"

SRC_URI[md5sum] = "bb8a98ee61cdc4bd835fdfd2b5cee3e6"
SRC_URI[sha256sum] = "175f275f5a041edda41fe0f27e59061d1d9c6615959475e4d68ad773b6a2e376"
SECTION = "x11/libs"
DEPENDS = "intltool xkbcomp-native glib-2.0"

PR = "r0"

EXTRA_OECONF = "--with-xkb-rules-symlink=xorg"

RDEPENDS_${PN} += "xkbcomp"
FILES_${PN} += "${datadir}/X11/xkb"

inherit autotools pkgconfig

do_install_append () {
    install -d ${D}/usr/share/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}
