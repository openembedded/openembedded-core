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

SRC_URI[archive.md5sum] = "930238dec55fdbf8eda9975b44f07b76"
SRC_URI[archive.sha256sum] = "48a8431974639c5a59c24fcd3ece1a19709872d5dfe78907524d9f5e9993f18f"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-glibtest \
                --disable-introspection"
