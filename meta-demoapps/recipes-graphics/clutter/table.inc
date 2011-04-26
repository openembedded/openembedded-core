DESCRIPTION = "Table Clutter Demo"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPLv2.1 & GPLv2"

DEPENDS = "clutter-gst-1.4 gnome-vfs"

inherit autotools pkgconfig

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${S}/table ${D}${bindir}/table
}

