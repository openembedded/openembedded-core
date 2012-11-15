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

SRC_URI="${XORG_MIRROR}/individual/data/xkeyboard-config/${BPN}-${PV}.tar.bz2"
SRC_URI[md5sum] = "6ad7b43062f123eacf8ff0eb3a4e0678"
SRC_URI[sha256sum] = "e43478a12fb0fe6757a7bad3a04fd3747ec53e53d5af22a9d9829dfb9aac8321"

SECTION = "x11/libs"
DEPENDS = "intltool-native xkbcomp-native glib-2.0 virtual/gettext"

PR = "r0"

EXTRA_OECONF = "--with-xkb-rules-symlink=xorg --disable-runtime-deps"

RDEPENDS_${PN} += "xkbcomp"
FILES_${PN} += "${datadir}/X11/xkb"

inherit autotools pkgconfig

do_install_append () {
    install -d ${D}${datadir}/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}
