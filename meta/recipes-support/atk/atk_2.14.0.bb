SUMMARY = "Accessibility toolkit for GNOME"
HOMEPAGE = "http://live.gnome.org/GAP/"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "x11/libs"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://atk/atkutil.c;endline=18;md5=6fd31cd2fdc9b30f619ca8d819bc12d3 \
                    file://atk/atk.h;endline=18;md5=fcd7710187e0eae485e356c30d1b0c3b"

DEPENDS = "glib-2.0"

inherit gnomebase gtk-doc

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "ecb7ca8469a5650581b1227d78051b8b"
SRC_URI[archive.sha256sum] = "2875cc0b32bfb173c066c22a337f79793e0c99d2cc5e81c4dac0d5a523b8fbad"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-glibtest \
                --disable-introspection"
