LICENSE = "GPLv2"
PRIORITY = "optional"
MAINTAINER = "Ross Burton <ross@openedhand.com>
PV = "0.0+svn${SRCDATE}"
DEPENDS = "gtk+ matchbox-panel-2"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

S = ${WORKDIR}/${PN}

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/matchbox-panel/*.so"
FILES_${PN}-dbg += "${libdir}/matchbox-panel/.debug"
