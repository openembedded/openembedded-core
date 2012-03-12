DESCRIPTION = "Custom MB session files for poky"
HOMEPAGE = "http://www.matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://matchbox-session;endline=20;md5=180f1c169a15d059a56c30094f6fb5ea"

SECTION = "x11"
RCONFLICTS = "matchbox-common"

SRC_URI = "file://matchbox-session"
S = "${WORKDIR}"

PR = "r1"

inherit update-alternatives

ALTERNATIVE_NAME = "x-session-manager"
ALTERNATIVE_LINK = "${bindir}/x-session-manager"
ALTERNATIVE_PATH = "${bindir}/matchbox-session"
ALTERNATIVE_PRIORITY = "10"

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${S}/matchbox-session ${D}/${bindir}
}
