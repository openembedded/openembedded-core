DESCRIPTION = "Touchscreen calibration data"
SECTION = "base"

PACKAGE_ARCH = ${MACHINE_ARCH}
SRC_URI = "file://pointercal"
S=${WORKDIR}

do_install() {
        install -d ${D}${sysconfdir}/
        install -m 0644 ${S}/pointercal ${D}${sysconfdir}/
}
