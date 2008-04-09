DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RCONFLICTS = "matchbox-common"

SRC_URI = "file://matchbox-session"
S = "${WORKDIR}"

inherit update-alternatives

ALTERNATIVE_NAME = "x-session-manager"
ALTERNATIVE_LINK = "${bindir}/x-session-manager"
ALTERNATIVE_PATH = "${bindir}/matchbox-session"
ALTERNATIVE_PRIORITY = "10"

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${S}/matchbox-session ${D}/${bindir}
}
