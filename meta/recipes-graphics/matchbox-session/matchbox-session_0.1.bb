SUMMARY = "Custom Matchbox session files"
DESCRIPTION = "Very simple session manager for matchbox tools"
HOMEPAGE = "http://www.matchbox-project.org/"
BUGTRACKER = "http://bugzilla.yoctoproject.org/"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://matchbox-session;endline=20;md5=180f1c169a15d059a56c30094f6fb5ea"

SECTION = "x11"
RCONFLICTS:${PN} = "matchbox-common"

SRC_URI = "file://matchbox-session \
           file://matchbox-session.desktop"

S = "${UNPACKDIR}"

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${S}/matchbox-session ${D}/${bindir}
	install -d ${D}${datadir}/xsessions
	install -m 0644 ${S}/matchbox-session.desktop ${D}${datadir}/xsessions
}

FILES:${PN} += "${datadir}/xsessions"
