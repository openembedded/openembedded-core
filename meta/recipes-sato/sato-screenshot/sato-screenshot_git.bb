DESCRIPTION = "An ultra-simple screen capture utility, aimed at handheld devices"
HOMEPAGE = "http://www.o-hand.com"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://main.c;endline=9;md5=023e14d6404d0a961eb97cbd011fc141 \
                    file://screenshot-ui.h;endline=9;md5=638d9ffa83e9325a36df224166ed6ad0"

DEPENDS = "matchbox-panel-2"
SRCREV = "c792e4edc758bab21e0b01814979eacf0b1af945"
PV = "0.1+git${SRCPV}"
PR = "r2"

SRC_URI = "git://git.yoctoproject.org/screenshot;protocol=git \
           file://fix_ldadd_order.patch"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/matchbox-panel/*.so"
FILES_${PN}-dbg += "${libdir}/matchbox-panel/.debug"

do_install_append () {
	rm ${D}${libdir}/matchbox-panel/*.la
}
