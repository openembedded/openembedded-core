LICENSE = "GPLv2"
PRIORITY = "optional"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
PV = "0.0+svn${SRCDATE}"
DEPENDS = "gtk+ startup-notification apmd"
PR = "r1"

RPROVIDES = matchbox-panel
RREPLACES = matchbox-panel
RCONFLICTS = matchbox-panel

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification --disable-libnotify"

S = ${WORKDIR}/${PN}

FILES_${PN} += "${libdir}/matchbox-panel/*.so \
                ${datadir}/matchbox-panel/battery/*.png"
FILES_${PN}-dbg += "${libdir}/matchbox-panel/.debug"

inherit autotools pkgconfig

do_stage() {
        autotools_stage_includes
}
