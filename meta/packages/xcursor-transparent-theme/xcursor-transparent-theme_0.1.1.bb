LICENSE = "GPL"
DESCRIPTION = "Transparent xcursor theme for handheld systems"
SECTION = "x11/base"
PR="r2"

SRC_URI = "http://matchbox-project.org/sources/utils/xcursor-transparent-theme-${PV}.tar.gz \
	   file://use-relative-symlinks.patch;patch=1 \
	   file://fix_watch_cursor.patch;patch=1"
FILES_${PN} = "${datadir}/icons/xcursor-transparent/cursors/*"

inherit autotools

PACKAGE_ARCH = "all"
