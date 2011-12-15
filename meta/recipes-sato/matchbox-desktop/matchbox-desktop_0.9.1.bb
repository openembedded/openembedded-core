DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPLv2.0+"
DEPENDS = "libmatchbox startup-notification"
SECTION = "x11/wm"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/matchbox-desktop/0.9/matchbox-desktop-${PV}.tar.bz2"

EXTRA_OECONF = "--enable-startup-notification --enable-dnotify"

inherit autotools pkgconfig

FILES_${PN} = "${bindir}/* \
	       ${datadir}/applications \
	       ${libdir}/matchbox/desktop/*.so \
	       ${datadir}/matchbox-desktop \
	       ${datadir}/pixmaps \
	       ${sysconfdir}/matchbox"

FILES_${PN}-dev += "${libdir}/matchbox-desktop \
		    ${includedir}/matchbox-desktop \
                    ${libdir}/matchbox/desktop/*.*a \
		    ${datadir}/matchbox/desktop/modules/*a"

FILES_${PN}-dbg += "${libdir}/matchbox/desktop/.debug/"
