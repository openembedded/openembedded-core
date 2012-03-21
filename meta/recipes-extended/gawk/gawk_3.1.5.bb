SUMMARY = "The GNU awk text processing utility"
DESCRIPTION = "The gawk package contains the GNU version of awk, a text processing \
utility. Awk interprets a special-purpose programming language to do \
quick and easy text pattern matching and reformatting jobs."
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

SRC_URI[md5sum] = "4760325489479cac17fe0114b8f62f30"
SRC_URI[sha256sum] = "463dcb9d0ca398b1d4f5a332f6cd9cec56441265fca616f2ea1b44d459e9f0f8"

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

do_install_append() {
	# remove the link since we don't package it
	rm ${D}${bindir}/awk
}
