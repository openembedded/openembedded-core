DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPL"
DEPENDS = "libmatchbox startup-notification"
SECTION = "x11/wm"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/matchbox-desktop/0.9/matchbox-desktop-${PV}.tar.bz2"

EXTRA_OECONF = "--enable-startup-notification --enable-dnotify"

inherit autotools pkgconfig

FILES_${PN} = "${bindir} \
	       ${datadir}/applications \
	       ${libdir}/matchbox/desktop/*.so \
	       ${datadir}/matchbox-desktop \
	       ${datadir}/pixmaps \
	       ${sysconfdir}/matchbox"

FILES_${PN}-dev = "${libdir}/matchbox-desktop \
		   ${includedir}/matchbox-desktop \
		   ${datadir}/matchbox/desktop/modules/*a"

do_stage() {
		install -d ${STAGING_INCDIR}/matchbox-desktop/
		install -m 0644 ${S}/src/*.h ${STAGING_INCDIR}/matchbox-desktop/

}
