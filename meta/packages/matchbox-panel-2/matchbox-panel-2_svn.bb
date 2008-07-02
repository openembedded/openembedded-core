LICENSE = "GPLv2"
PRIORITY = "optional"
DEPENDS = "gtk+ startup-notification apmd dbus dbus-glib"
PV = "0.0+svnr${SRCREV}"
PR = "r3"

RPROVIDES_${PN} = "matchbox-panel"
RREPLACES_${PN} = "matchbox-panel"
RCONFLICTS_${PN} = "matchbox-panel"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification --enable-dbus"

S = "${WORKDIR}/${PN}"

FILES_${PN} += "${libdir}/matchbox-panel/*.so \
                ${datadir}/matchbox-panel/brightness/*.png \
                ${datadir}/matchbox-panel/startup/*.png "
FILES_${PN}-dbg += "${libdir}/matchbox-panel/.debug"

inherit autotools pkgconfig

do_stage() {
        autotools_stage_includes
}
