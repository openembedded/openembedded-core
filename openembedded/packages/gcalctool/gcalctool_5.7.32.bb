LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "gtk+"
MAINTAINER = "Ross Burton <ross@openedhand.com>"
DESCRIPTION = "gcalctool is a powerful calculator
PR = "r1"

SRC_URI = "http://download.gnome.org/sources/${PN}/5.7/${PN}-${PV}.tar.gz \
        file://makefile-fix.diff;patch=1"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-gnome"
