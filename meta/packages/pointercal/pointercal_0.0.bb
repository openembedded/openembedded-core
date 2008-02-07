DESCRIPTION = "Touchscreen calibration data"
SECTION = "base"
PR = "r8"

SRC_URI = "file://pointercal"
S = "${WORKDIR}"

do_install() {
	# Only install file if it has a contents
	if [ -s ${S}/pointercal ]; then
	        install -d ${D}${sysconfdir}/
	        install -m 0644 ${S}/pointercal ${D}${sysconfdir}/
	fi
}

ALLOW_EMPTY_${PN} = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

