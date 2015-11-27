SUMMARY = "Cascading Style Sheet (CSS) parsing and manipulation toolkit"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://src/cr-rgb.c;endline=22;md5=31d5f0944d556c8589d04ea6055fcc66 \
                    file://tests/cr-test-utils.c;endline=21;md5=2382c27934cae1d3792fcb17a6142c4e"

SECTION = "x11/utils"
DEPENDS = "glib-2.0 libxml2 zlib"
BBCLASSEXTEND = "native"
EXTRA_OECONF += "--enable-Bsymbolic=auto"
PR = "r2"

BINCONFIG = "${bindir}/croco-0.6-config"

inherit autotools pkgconfig gnomebase gtk-doc binconfig-disabled

SRC_URI[archive.md5sum] = "f1863da805c9206563da06f56da1ea55"
SRC_URI[archive.sha256sum] = "38b9a6aed1813e55b3ca07a68d1af845ad4d1f984602e9272fe692930c0be0ae"
