DESCRIPTION = "A program that you can use to select particular records in a \
file and perform operations upon them."
HOMEPAGE = "www.gnu.org/software/gawk"
BUGTRACKER  = "bug-gawk@gnu.org"
SECTION = "console/utils"

# gawk <= 3.1.5: GPLv2
# gawk >= 3.1.6: GPLv3
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

RDEPENDS_gawk += "gawk-common"
RDEPENDS_pgawk += "gawk-common"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz"

SRC_URI[md5sum] = "35937a0f83f0efe7a8c2dee635624784"
SRC_URI[sha256sum] = "2146b3cc7a2d2b16a9457e73f14a3cb51a4292575425ed8f16f7e0a5e4f1a50d"

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
