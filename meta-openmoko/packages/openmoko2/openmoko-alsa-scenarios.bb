DESCRIPTION = "Package for the different scenarios used by Openmoko"
SECTION = "openmoko/base"
PV = "1.0+svnr${SRCREV}"
PR = "r1"

COMPATIBLE_MACHINE = "(om-gta01|om-gta02)"

SRC_URI_${MACHINE_ARCH} = "svn://svn.openmoko.org/trunk/src/target/audio/;module=${MACHINE_ARCH};proto=http"

S = "${WORKDIR}/${MACHINE_ARCH}"

do_install() {
    install -d ${D}${datadir}/openmoko/scenarios/
    install -m 644 ${S}/*.state ${D}${datadir}/openmoko/scenarios/
    install -d ${D}${sysconfdir}/
    install -m 644 ${S}/stereoout.state ${D}${sysconfdir}/asound.state
}

PACKAGE_ARCH_${PN} = "${MACHINE_ARCH}"

FILES_${PN} += "${datadir}/openmoko"
