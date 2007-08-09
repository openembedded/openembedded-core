LICENSE = "GPLv2"
PRIORITY = "optional"
DEPENDS = "gtk+ startup-notification apmd"
PV = "0.0+svnr${SRCREV}"

RPROVIDES = "matchbox-panel"
RREPLACES = "matchbox-panel"
RCONFLICTS = "matchbox-panel"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification --disable-libnotify"

S = "${WORKDIR}/${PN}"

FILES_${PN} += "${libdir}/matchbox-panel/*.so \
                ${datadir}/matchbox-panel/battery/*.png \
                ${datadir}/matchbox-panel/startup/*.png "
FILES_${PN}-dbg += "${libdir}/matchbox-panel/.debug"

inherit autotools pkgconfig

do_stage() {
        autotools_stage_includes
}
