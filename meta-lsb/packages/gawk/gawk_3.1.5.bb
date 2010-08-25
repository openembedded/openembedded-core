DESCRIPTION = "A program that you can use to select particular records in a \
file and perform operations upon them."
HOMEPAGE = "www.gnu.org/software/gawk"
BUGTRACKER  = "bug-gawk@gnu.org"
SECTION = "console/utils"

# gawk <= 3.1.5: GPLv2
# gawk >= 3.1.6: GPLv3
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

RDEPENDS_gawk += "gawk-common"
RDEPENDS_pgawk += "gawk-common"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz"

inherit autotools gettext update-alternatives

PACKAGES += "gawk-common pgawk"

FILES_${PN} = "${bindir}/gawk* ${bindir}/igawk"
FILES_gawk-common += "${datadir}/awk/* ${libexecdir}/awk/*"
FILES_pgawk = "${bindir}/pgawk*"
FILES_${PN}-dbg += "${libexecdir}/awk/.debug"

ALTERNATIVE_NAME = "awk"
ALTERNATIVE_PATH = "gawk"
ALTERNATIVE_LINK = "${bindir}/awk"
ALTERNATIVE_PRIORITY = "100"
