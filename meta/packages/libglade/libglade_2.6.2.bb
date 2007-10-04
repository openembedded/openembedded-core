LICENSE = "LGPL"
DESCRIPTION = "Runtime support for GTK interface builder"
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

do_stage () {
        autotools_stage_all
}
