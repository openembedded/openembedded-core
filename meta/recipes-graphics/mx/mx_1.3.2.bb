DESCRIPTION = "Clutter based widget library"
LICENSE = "LGPLv2.1"
PR = "r1"

DEPENDS = "clutter-1.8"

inherit autotools

SRC_URI = "http://source.clutter-project.org/sources/mx/1.3/mx-${PV}.tar.bz2"
SRC_URI[md5sum] = "6057f3403d1109191dc7ef492bae5867"
SRC_URI[sha256sum] = "94fd2b307b204945643af1e3193a2a7be96712f9296cf0e525f5fd7538f85513"

LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=fbc093901857fcd118f065f900982c24 \
                    file://mx/mx-widget.c;beginline=8;endline=20;md5=13bba3c973a72414a701e1e87b5ee879"

EXTRA_OECONF = "--disable-introspection --disable-gtk-widgets"

