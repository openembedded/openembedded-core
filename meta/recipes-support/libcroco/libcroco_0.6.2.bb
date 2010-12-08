DESCRIPTION = "The Libcroco project is an effort to build a generic Cascading Style Sheet (CSS) parsing and manipulation toolkit"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://src/cr-rgb.c;endline=25;md5=1df8189094ba7bbed953225785032826 \
                    file://tests/cr-test-utils.c;endline=21;md5=2382c27934cae1d3792fcb17a6142c4e"

SECTION = "x11/utils"
DEPENDS = "glib-2.0 libxml2 zlib"
PR = "r0"

inherit autotools pkgconfig gnome

SRC_URI_append = " file://croco.patch;apply=yes "

SRC_URI[archive.md5sum] = "1429c597aa4b75fc610ab3a542c99209"
SRC_URI[archive.sha256sum] = "be24853f64c09b63d39e563fb0222e29bae1a33c3d9f6cbffc0bc27669371749"
