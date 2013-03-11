DESCRIPTION = "A program that you can use to select particular records in a \
file and perform operations upon them."
HOMEPAGE = "www.gnu.org/software/gawk"
BUGTRACKER  = "bug-gawk@gnu.org"
SECTION = "console/utils"

# gawk <= 3.1.5: GPLv2
# gawk >= 3.1.6: GPLv3
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "readline"

RDEPENDS_gawk += "gawk-common"
RDEPENDS_pgawk += "gawk-common"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz"

SRC_URI[md5sum] = "4d505dc2c9f1eb3e9f8d6cac87d4bd1a"
SRC_URI[sha256sum] = "6e0de117c3713aa8d7fa347fc9fd645b10038ae49d8cf947d8c1d51cbb76141a"

inherit autotools gettext update-alternatives

PACKAGES += "gawk-common pgawk dgawk"

FILES_${PN} = "${bindir}/gawk* ${bindir}/igawk"
FILES_gawk-common += "${datadir}/awk/* ${libexecdir}/awk/*"
FILES_pgawk = "${bindir}/pgawk*"
FILES_${PN}-dbg += "${libexecdir}/awk/.debug"
FILES_dgawk = "${bindir}/dgawk*"

ALTERNATIVE_${PN} = "awk"
ALTERNATIVE_TARGET[awk] = "${bindir}/gawk"
ALTERNATIVE_PRIORITY = "100"

do_install_append() {
	# remove the link since we don't package it
	rm ${D}${bindir}/awk
}
