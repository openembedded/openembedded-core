SUMMARY = "Text-based modem control and terminal emulation program"
HOMEPAGE = "https://salsa.debian.org/minicom-team/minicom"
DESCRIPTION = "Minicom is a text-based modem control and terminal emulation program for Unix-like operating systems"
SECTION = "console/network"
DEPENDS = "ncurses virtual/libiconv"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=420477abc567404debca0a2a1cb6b645 \
                    file://src/minicom.h;beginline=1;endline=12;md5=a58838cb709f0db517f4e42730c49e81"

SRC_URI = "${DEBIAN_MIRROR}/main/m/${BPN}/${BPN}_${PV}.orig.tar.bz2"

SRC_URI[sha256sum] = "90e7ce2856b3eaaa3f452354d17981c49d32c426a255b6f0d3063a227c101538"

PACKAGECONFIG ??= ""
PACKAGECONFIG[lockdev] = "--enable-lockdev,--disable-lockdev,lockdev"

inherit autotools gettext pkgconfig

do_install() {
	for d in doc extras man lib src; do make -C $d DESTDIR=${D} install; done
}

RRECOMMENDS:${PN} += "lrzsz"

RDEPENDS:${PN} += "ncurses-terminfo-base"
