DESCRIPTION = "Touchscreen calibration data"
SECTION = "base"

SRC_URI = "file://pointercal"
S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
        install -d ${D}${sysconfdir}/
        install -m 0644 ${S}/pointercal ${D}${sysconfdir}/
}
