DESCRIPTION = "Runtime support for GTK interface builder"
HOMEPAGE = "http://library.gnome.org/devel/libglade/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://glade/glade.h;endline=22;md5=a04f461c964ba4b57a172d1fbcd8f8fc \
                    file://glade/glade-gtk.c;endline=22;md5=766f993433e2642fec87936d319990ff"

SECTION = "libs"
PR = "r0"
PRIORITY = "optional"
DEPENDS = "gtk+ gtk-doc-native"

inherit autotools pkgconfig gnome

SRC_URI += "file://glade-cruft.patch;patch=1 file://no-xml2.patch;patch=1"

EXTRA_OECONF += "--without-libxml2"

CFLAGS += "-lz"

PACKAGES += " ${PN}-data"
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-data = "${datadir}/xml/libglade/glade-2.0.dtd"
FILES_${PN}-dev += "${bindir}/libglade-convert"
#RDEPENDS_${PN} = "${PN}-data"
