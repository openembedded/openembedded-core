SUMMARY = "Library for caching application menus"
DESCRIPTION = "A library creating and utilizing caches to speed up freedesktop.org application menus"
HOMEPAGE = "http://lxde.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://libmenu-cache/menu-cache.h;endline=29;md5=ca0101a419b3584963e0c45f2b6b55ef \
                    file://menu-cache-daemon/menu-cached.c;endline=22;md5=fcecb7d315c57ef804103fa9cdab7111"

SECTION = "x11/libs"
DEPENDS = "glib-2.0 zlib"

SRC_URI = "${SOURCEFORGE_MIRROR}/lxde/menu-cache-${PV}.tar.gz"

SRC_URI[md5sum] = "ac4a9ea77db68d3db3f9f53cc75af66a"
SRC_URI[sha256sum] = "6b7c1627b5102d8301a8a3e845f673a7181a71dde32f6455abf22d03e392b89f"

PR = "r2"

inherit autotools pkgconfig
