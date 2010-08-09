SECTION = "console/network"
DEPENDS = "ncurses"
LICENSE = "GPL"
SRC_URI = "http://alioth.debian.org/download.php/123/minicom-${PV}.tar.gz \
	file://configure.patch;patch=1 \
	file://gcc4-scope.patch;patch=1"

inherit autotools gettext

do_install() {
	for d in doc extras man intl lib src; do make -C $d DESTDIR=${D} install; done
}
