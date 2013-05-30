DESCRIPTION = "An accessibility toolkit for GNOME."
HOMEPAGE = "http://live.gnome.org/GAP/"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "x11/libs"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://atk/atkutil.c;endline=20;md5=db21b0bdbef9da4dc6eb122debc9f9bc \
                    file://atk/atk.h;endline=20;md5=c58238d688c24387376d6c69d06248a7"

DEPENDS = "glib-2.0"

inherit autotools gtk-doc pkgconfig

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "c652bd25530825d604dae1c1ebd2da02"
SRC_URI[sha256sum] = "b22519176226f3e07cf6d932b77852e6b6be4780977770704b32d0f4e0686df4"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-glibtest \
                --disable-introspection"
