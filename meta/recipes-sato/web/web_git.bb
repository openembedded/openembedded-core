LICENSE = "GPLv2"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade gtkhtml2 curl gconf js libowl"
DESCRIPTION = "Web is a multi-platform web browsing application."
PR = "r1"

SRCREV = "96da839f65e17ecc6d5261c1d74accd88423dd7a"
PV = "0.0+git${SRCPV}"

SRC_URI = 	"git://git.yoctoproject.org/web-sato;protocol=git \
		file://owl-window-menu.patch \
		file://fix_makefile.patch \
		"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}/git"

do_unpack_append () {
	bb.build.exec_func('do_remove_patches', d)
}

do_remove_patches () {
	rm -rf ${S}/patches
}

inherit autotools pkgconfig gconf

