DESCRIPTION = "An accessibility toolkit for GNOME."
HOMEPAGE = "http://live.gnome.org/GAP/"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "x11/libs"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://atk/atkutil.c;endline=20;md5=db21b0bdbef9da4dc6eb122debc9f9bc \
                    file://atk/atk.h;endline=20;md5=c58238d688c24387376d6c69d06248a7"
PR = "r0"

DEPENDS = "glib-2.0"

inherit autotools gtk-doc pkgconfig

SRC_URI = "http://download.gnome.org/sources/atk/2.6/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "6b34e2a63dda4429b5692be7ca3aa5bf"
SRC_URI[sha256sum] = "eff663f90847620bb68c9c2cbaaf7f45e2ff44163b9ab3f10d15be763680491f"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-glibtest \
                --disable-introspection"
