DESCRIPTION = "Gtk+ Theme Benchmark Program"
DEPENDS = "gtk+"
HOMEPAGE = "http://wiki.laptop.org/go/GTK_for_OLPC"
SECTION = "devel"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://appwindow.c;endline=13;md5=8c09920de460c7ea1f64ee56986aabd9"

PV = "0.0.0+git${SRCDATE}"

SRC_URI = "git://dev.laptop.org/projects/soc-gtk/;protocol=git"
S = "${WORKDIR}/git/gtk-theme-torturer"

CFLAGS += "-Wl,-rpath-link,${STAGING_LIBDIR}"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 torturer ${D}${bindir}
}


