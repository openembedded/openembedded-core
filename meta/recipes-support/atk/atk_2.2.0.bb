DESCRIPTION = "An accessibility toolkit for GNOME."
HOMEPAGE = "http://live.gnome.org/GAP/"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "x11/libs"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://atk/atkutil.c;endline=20;md5=db21b0bdbef9da4dc6eb122debc9f9bc \
                    file://atk/atk.h;endline=20;md5=c58238d688c24387376d6c69d06248a7"
PR = "r2"

DEPENDS = "glib-2.0 gtk-doc-native"

inherit autotools pkgconfig

SRC_URI = "http://download.gnome.org/sources/atk/2.2/${BPN}-${PV}.tar.bz2"
SRC_URI[md5sum] = "4894e9b04f0a9f1c37a624a1e8d6d73f"
SRC_URI[sha256sum] = "d201e3f5808aef0b1aec2277bfa61074f68863e405428adb57a73aab5c838450"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-glibtest \
                --disable-introspection"
