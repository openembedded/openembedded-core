DESCRIPTION = "eeePC specific ACPI scripts"
HOMEPAGE = "http://alioth.debian.org/projects/debian-eeepc/"
SECTION = "base"

LICENSE="GPL"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=77ef83ab5f4af938a93edb61f7b74f2c"

SRCREV = "e224937ba51165e2a52252f9a113c8a82c01bb36"
PV = "1.1.11+git${SRCPV}"
PR = "r4"

RDEPENDS_${PN} = "pm-utils"

SRC_URI = "git://git.debian.org/git/debian-eeepc/eeepc-acpi-scripts.git;protocol=git \
	   file://remove-doc-check.patch \
	   file://powerbtn.patch \
	   file://policy-funcs "

S = "${WORKDIR}/git"

FILES_${PN} = "${datadir}/acpi-support/ \
		${datadir}/eeepc-acpi-scripts \
		${sysconfdir}/default/ \
		${sysconfdir}/acpi/"

do_install () {
	install -d ${D}${sysconfdir}/default/
	install -d ${D}${sysconfdir}/acpi/actions/
	install -d ${D}${sysconfdir}/acpi/events/
	install -d ${D}${sysconfdir}/acpi/lib/udev/rules.d
	install -d ${D}${datadir}/eeepc-acpi-scripts/
	install -d ${D}${datadir}/acpi-support/
	install -m 644 ${S}/events/* ${D}${sysconfdir}/acpi/events/
	install -m 644 ${S}/lib/udev/rules.d/* ${D}${sysconfdir}/acpi/lib/udev/rules.d/
	install ${S}/actions/* ${D}${sysconfdir}/acpi/actions/
	install -m 0644 ${S}/acpilib/functions.sh ${D}${datadir}/eeepc-acpi-scripts/
	install -m 0644 ${WORKDIR}/policy-funcs ${D}${datadir}/acpi-support/
	install -m 0644 ${S}/debian/eeepc-acpi-scripts.default* ${D}${sysconfdir}/default/
}
