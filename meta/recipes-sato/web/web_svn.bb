LICENSE = "GPLv2"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade gtkhtml2 curl gconf js libowl"
DESCRIPTION = "Web is a multi-platform web browsing application."
PR = "r4"

SRCREV = "129"
PV = "0.0+svnr${SRCPV}"

SRC_URI = 	"svn://svn.o-hand.com/repos/${BPN};module=trunk;proto=http \
		file://owl-window-menu.patch \
		"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}/trunk"

do_unpack_append () {
	bb.build.exec_func('do_remove_patches', d)
}

do_remove_patches () {
	rm -rf ${S}/patches
}

inherit autotools pkgconfig gconf

