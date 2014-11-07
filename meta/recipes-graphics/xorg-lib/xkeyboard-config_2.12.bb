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
SRC_URI[md5sum] = "1fd54ceb9092d1dbcaabaf03653092bc"
SRC_URI[sha256sum] = "65b62b95b77b609cb6c0439e0148c48c3ab7dcb5c90eb8d34cf1cb8f360cca44"

SECTION = "x11/libs"
DEPENDS = "intltool-native virtual/gettext util-macros libxslt-native"

EXTRA_OECONF = "--with-xkb-rules-symlink=xorg --disable-runtime-deps"

FILES_${PN} += "${datadir}/X11/xkb"

inherit autotools pkgconfig gettext

do_install_append () {
    install -d ${D}${datadir}/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}
