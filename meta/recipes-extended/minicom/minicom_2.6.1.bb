SUMMARY = "Text-based modem control and terminal emulation program"
DESCRIPTION = "Minicom is a text-based modem control and terminal emulation program for Unix-like operating systems"
SECTION = "console/network"
DEPENDS = "ncurses"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=420477abc567404debca0a2a1cb6b645 \
                    file://src/minicom.h;beginline=1;endline=12;md5=a58838cb709f0db517f4e42730c49e81"

SRC_URI = "http://alioth.debian.org/frs/download.php/3700/minicom-${PV}.tar.gz \
	file://rename-conflicting-functions.patch \
	"
#	file://gcc4-scope.patch

SRC_URI[md5sum] = "435fb410a5bfa9bb20d4248b3ca53529"
SRC_URI[sha256sum] = "a6e7bf533c3796f3a67a7d109f328d46497c687ed13885bd1be4ce0548fc4f56"

inherit autotools gettext

do_install() {
	for d in doc extras man lib src; do make -C $d DESTDIR=${D} install; done
}

