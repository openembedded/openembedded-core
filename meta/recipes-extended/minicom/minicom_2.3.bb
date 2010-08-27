SECTION = "console/network"
DEPENDS = "ncurses"
LICENSE = "GPL"
SRC_URI = "http://alioth.debian.org/frs/download.php/2332/minicom-${PV}.tar.gz \
	file://rename-conflicting-functions.patch \
	"

#	file://gcc4-scope.patch;patch=1 \

inherit autotools gettext

do_install() {
	for d in doc extras man lib src; do make -C $d DESTDIR=${D} install; done
}

SRC_URI[md5sum] = "0ebe7a91898384ca906787cc4e2c3f25"
SRC_URI[sha256sum] = "2acbc3d4a07e1134ee285a72fa44bbc27703b02dba02be68db9e6fd8320356fb"
