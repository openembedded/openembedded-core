SUMMARY = "Library for caching application menus"
DESCRIPTION = "A library creating and utilizing caches to speed up freedesktop.org application menus"
HOMEPAGE = "http://lxde.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://libmenu-cache/menu-cache.h;endline=29;md5=21625ca49a07e7c381027c63eff769dc \
                    file://menu-cache-daemon/menu-cached.c;endline=22;md5=fcecb7d315c57ef804103fa9cdab7111"

SECTION = "x11/libs"
DEPENDS = "glib-2.0 zlib"

SRC_URI = "${SOURCEFORGE_MIRROR}/lxde/menu-cache-${PV}.tar.gz"

SRC_URI[md5sum] = "a14b0b162cd64d56c16bf6af16f3a47f"
SRC_URI[sha256sum] = "07241c1f5f371b426d3b0a6e571a86184ec6256bdd7ead7a4da866cd10f25955"

PR = "r0"

inherit autotools pkgconfig
