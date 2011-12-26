SUMMARY = "Text-based modem control and terminal emulation program"
DESCRIPTION = "Minicom is a text-based modem control and terminal emulation program for Unix-like operating systems"
SECTION = "console/network"
DEPENDS = "ncurses"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=420477abc567404debca0a2a1cb6b645 \
                    file://src/minicom.h;beginline=1;endline=12;md5=a58838cb709f0db517f4e42730c49e81"

SRC_URI = "http://alioth.debian.org/frs/download.php/3487/minicom-${PV}.tar.gz \
	file://rename-conflicting-functions.patch \
	"
#	file://gcc4-scope.patch

SRC_URI[md5sum] = "a5117d4d21e2c9e825edb586ee2fe8d2"
SRC_URI[sha256sum] = "2aa43f98580d3c9c59b12895f15783695cde85472f6bfc7974bcc0935af0a8fd"

inherit autotools gettext

do_install() {
	for d in doc extras man lib src; do make -C $d DESTDIR=${D} install; done
}

