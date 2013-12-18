SUMMARY = "Text-based modem control and terminal emulation program"
DESCRIPTION = "Minicom is a text-based modem control and terminal emulation program for Unix-like operating systems"
SECTION = "console/network"
DEPENDS = "ncurses virtual/libiconv"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=420477abc567404debca0a2a1cb6b645 \
                    file://src/minicom.h;beginline=1;endline=12;md5=a58838cb709f0db517f4e42730c49e81"

PR = "r0"

SRC_URI = "http://alioth.debian.org/frs/download.php/3869/minicom-${PV}.tar.gz \
           file://allow.to.disable.lockdev.patch \
"

SRC_URI[md5sum] = "203c56c4b447f45e2301b0cc4e83da3c"
SRC_URI[sha256sum] = "f3cf215f7914ffa5528e398962176102ad74df27ba38958142f56aa6d15c9168"

PACKAGECONFIG ??= ""
PACKAGECONFIG[lockdev] = "--enable-lockdev,--disable-lockdev,lockdev"

inherit autotools gettext

do_install() {
	for d in doc extras man lib src; do make -C $d DESTDIR=${D} install; done
}

