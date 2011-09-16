LICENSE = "GPLv2"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade gtkhtml2 curl gconf js libowl"
DESCRIPTION = "Web is a multi-platform web browsing application."
PR = "r0"

SRCREV = "e9a1c9433b9500feb7e3b9bb135debcea3a3e651"
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

