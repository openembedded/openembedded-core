SUMMARY = "Accessibility toolkit for GNOME"
HOMEPAGE = "http://live.gnome.org/GAP/"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "x11/libs"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://atk/atkutil.c;endline=20;md5=db21b0bdbef9da4dc6eb122debc9f9bc \
                    file://atk/atk.h;endline=20;md5=c58238d688c24387376d6c69d06248a7"

DEPENDS = "glib-2.0"

inherit gnomebase gtk-doc

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "e77833d4445ebe6842e9f9a0667b0be7"
SRC_URI[archive.sha256sum] = "636917a5036bc851d8491194645d284798ec118919a828be5e713b6ecc5b50b0"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-glibtest \
                --disable-introspection"
