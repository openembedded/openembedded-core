LICENSE = "LGPL"
DESCRIPTION = "Runtime support for GTK interface builder"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "gtk+ gtk-doc"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"

inherit autotools pkgconfig gnome

PR = "r0"

SRC_URI += "file://glade-cruft.patch;patch=1 file://no-xml2.patch;patch=1"

EXTRA_OECONF += "--without-libxml2"

CFLAGS += "-lz"

PACKAGES += " ${PN}-data"
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-data = "${datadir}/xml/libglade/glade-2.0.dtd"
FILES_${PN}-dev += "${bindir}/libglade-convert"
#RDEPENDS_${PN} = "${PN}-data"

headers = "glade-build.h glade-init.h glade-parser.h glade-xml.h glade.h"

do_stage () {
	oe_libinstall -a -so -C glade libglade-2.0 ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/libglade-2.0/glade
	for i in ${headers}; do
		install -m 0644 ${S}/glade/$i ${STAGING_INCDIR}/libglade-2.0/glade/$i
	done
}
