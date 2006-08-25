LICENSE = "GPLv2"
PRIORITY = "optional"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
PV = "0.0+svn${SRCDATE}"
DEPENDS = "gtk+ startup-notification libapm"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification --disable-libnotify"

S = ${WORKDIR}/${PN}

FILES_${PN} += "${libdir}/matchbox-panel/*.so.* \
                $(datadir}/matchbox-panel"
FILES_${PN}-dev += "${libdir}/matchbox-panel/*.so ${libdir}/matchbox-panel/*.la \
		   ${libdir}/matchbox-panel/*.a"

inherit autotools pkgconfig

