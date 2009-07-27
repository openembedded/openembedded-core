
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://configurefix.patch;patch=1;rev=e1d63681739dd16195d005b3cf15752df294bd3c"
PV = "0.0.1+git${SRCPV}"
PR = "r1"

S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/desktop-directories/*"

inherit autotools_stage
