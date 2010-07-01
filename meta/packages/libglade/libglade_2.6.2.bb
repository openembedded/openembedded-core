DESCRIPTION = "Runtime support for GTK interface builder"
HOMEPAGE = "http://library.gnome.org/devel/libglade/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2+"

SECTION = "libs"
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
