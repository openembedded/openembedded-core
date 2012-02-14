DESCRIPTION = "Mini X session files for poky"
HOMEPAGE = "http://www.yoctoproject.org"
BUGTRACKER = "http://bugzilla.pokylinux.org"

PR = "r1"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://mini-x-session;endline=5;md5=b6430bffbcf05f9760e72938826b7487"

SECTION = "x11"
RCONFLICTS = "matchbox-common"

SRC_URI = "file://mini-x-session"
S = "${WORKDIR}"

inherit update-alternatives

ALTERNATIVE_NAME = "x-session-manager"
ALTERNATIVE_LINK = "${bindir}/x-session-manager"
ALTERNATIVE_PATH = "${bindir}/mini-x-session"
ALTERNATIVE_PRIORITY = "50"

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${S}/mini-x-session ${D}/${bindir}
}
