LICENSE = "GPLv2"
PRIORITY = "optional"
DEPENDS = "matchbox-panel-2"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

S = ${WORKDIR}/${PN}

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/matchbox-panel/*.so"
FILES_${PN}-dbg += "${libdir}/matchbox-panel/.debug"
