SECTION = "base"
DESCRIPTION = "eeePC specific ACPI scripts"
LICENSE="GPL"

SRCREV = "df785afdacedf869b20fb88a793c13b539819270"

PR = "r2"

RDEPENDS = "pm-utils"

SRC_URI = "git://git.debian.org/git/debian-eeepc/eeepc-acpi-scripts.git;protocol=git \
	   file://remove-doc-check.patch;patch=1 \
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
	install -d ${D}${sysconfdir}/acpi/lib/
	install -d ${D}${datadir}/eeepc-acpi-scripts/
	install -d ${D}${datadir}/acpi-support/
	install -m 644 ${S}/events/* ${D}${sysconfdir}/acpi/events/
	install -m 644 ${S}/lib/* ${D}${sysconfdir}/acpi/lib/
	install ${S}/actions/* ${D}${sysconfdir}/acpi/actions/
	install -m 0644 ${S}/functions.sh ${D}${datadir}/eeepc-acpi-scripts/
	install -m 0644 ${WORKDIR}/policy-funcs ${D}${datadir}/acpi-support/
	install -m 0644 ${S}/debian/eeepc-acpi-scripts.default ${D}${sysconfdir}/default/
}
