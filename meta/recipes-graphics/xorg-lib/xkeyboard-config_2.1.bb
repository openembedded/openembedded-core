SUMMARY = "Keyboard configuration database for X Window"

DESCRIPTION = "The non-arch keyboard configuration database for X \
Window.  The goal is to provide the consistent, well-structured, \
frequently released open source of X keyboard configuration data for X \
Window System implementations.  The project is targeted to XKB-based \
systems."

HOMEPAGE = "http://freedesktop.org/wiki/Software/XKeyboardConfig"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=xkeyboard-config"

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=0e7f21ca7db975c63467d2e7624a12f9"

SRC_URI = "http://people.freedesktop.org/~svu/xkeyboard-config-${PV}.tar.bz2"
SRC_URI[md5sum] = "6ce65480445fb2d9c071ad1f002a7675"
SRC_URI[sha256sum] = "e293aa4b0dd259dbb4f0e7f56fdd95db5047d052c7b3b80922fe5663923a805d"

SECTION = "x11/libs"
DEPENDS = "intltool-native xkbcomp-native glib-2.0"

PR = "r1"

EXTRA_OECONF = "--with-xkb-rules-symlink=xorg"

RDEPENDS_${PN} += "xkbcomp"
FILES_${PN} += "${datadir}/X11/xkb"

inherit autotools pkgconfig

do_install_append () {
    install -d ${D}/usr/share/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}
