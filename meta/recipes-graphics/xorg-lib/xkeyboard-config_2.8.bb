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
SRC_URI[md5sum] = "e66d567d85a954d589c5723fb0e1f3dd"
SRC_URI[sha256sum] = "4e1cf5468d488aaf320ddfd439e03cbda0b213550fd734026d23138e4b2078ae"

SECTION = "x11/libs"
DEPENDS = "intltool-native virtual/gettext util-macros"

PR = "r0"

EXTRA_OECONF = "--with-xkb-rules-symlink=xorg --disable-runtime-deps"

RDEPENDS_${PN} += "xkbcomp"
FILES_${PN} += "${datadir}/X11/xkb"

inherit autotools pkgconfig

do_install_append () {
    install -d ${D}${datadir}/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}
