DESCRIPTION = "Matchbox virtual keyboard for X11"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"
SECTION = "x11"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/matchbox-keyboard.h;endline=20;md5=4ba16ff913ad245dd6d95a6c67f72526 \
                    file://applet/applet.c;endline=20;md5=e9201b3efa0a81a160b88d6feb5cf75b"

DEPENDS = "libfakekey expat libxft gtk+ matchbox-panel-2"

SRCREV = "b38f24036cff3be6c2fbcf9ca9881803e69003ac"
PV = "0.0+git${SRCPV}"
PR = "r4"

SRC_URI = "git://git.yoctoproject.org/${BPN} \
           file://configure_fix.patch;maxrev=1819 \
           file://single-instance.patch \
           file://80matchboxkeyboard.shbg \
           file://png-fix.patch"

S = "${WORKDIR}/git"

inherit autotools pkgconfig gettext gtk-immodules-cache

EXTRA_OECONF = "--disable-cairo --enable-gtk-im --enable-applet"

PACKAGES += "${PN}-im ${PN}-applet"

FILES_${PN} = "${bindir}/ \
	       ${sysconfdir} \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
	       ${datadir}/matchbox-keyboard"

FILES_${PN}-dbg += "${libdir}/gtk-2.0/*/immodules/.debug"

FILES_${PN}-im = "${libdir}/gtk-2.0/*/immodules/*.so"

FILES_${PN}-applet = "${libdir}/matchbox-panel/*.so"


do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d/
	install -m 755 ${WORKDIR}/80matchboxkeyboard.shbg ${D}/${sysconfdir}/X11/Xsession.d/

	rm -f ${D}${libdir}/gtk-2.0/*/immodules/*.la
	rm -f ${D}${libdir}/matchbox-panel/*.la
}

GTKIMMODULES_PACKAGES = "${PN}-im"

RDEPENDS_${PN} = "formfactor dbus-wait"
RRECOMMENDS_${PN} = "${PN}-applet"
