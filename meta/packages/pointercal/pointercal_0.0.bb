DESCRIPTION = "Touchscreen calibration data"
SECTION = "base"

COMPATIBLE_MACHINE = "(spitz|c7x0|nokia800|akita)"

SRC_URI = "file://pointercal"
S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
        install -d ${D}${sysconfdir}/
        install -m 0644 ${S}/pointercal ${D}${sysconfdir}/
}
