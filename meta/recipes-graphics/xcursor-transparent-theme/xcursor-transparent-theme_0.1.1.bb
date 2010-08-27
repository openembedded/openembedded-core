DESCRIPTION = "Transparent xcursor theme for handheld systems"
HOMEPAGE = "http://www.matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SECTION = "x11/base"
PR="r3"

SRC_URI = "http://matchbox-project.org/sources/utils/xcursor-transparent-theme-${PV}.tar.gz \
	   file://use-relative-symlinks.patch;patch=1 \
	   file://fix_watch_cursor.patch;patch=1"
FILES_${PN} = "${datadir}/icons/xcursor-transparent/cursors/*"

inherit autotools

PACKAGE_ARCH = "all"
