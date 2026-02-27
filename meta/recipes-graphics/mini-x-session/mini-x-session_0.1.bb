SUMMARY = "Very simple session manager for X"
DESCRIPTION = "Simple session manager for X, that provides just the right boilerplate to create a session and launch the browser "
HOMEPAGE = "http://www.yoctoproject.org"
BUGTRACKER = "http://bugzilla.pokylinux.org"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://mini-x-session;endline=5;md5=b6430bffbcf05f9760e72938826b7487"

SECTION = "x11"
RCONFLICTS:${PN} = "matchbox-common"

SRC_URI = "file://mini-x-session \
           file://mini-x-session.desktop"

S = "${UNPACKDIR}"

RDEPENDS:${PN} = "sudo"

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${S}/mini-x-session ${D}/${bindir}
	install -d ${D}${datadir}/xsessions
	install -m 0644 ${S}/mini-x-session.desktop ${D}${datadir}/xsessions
}

FILES:${PN} += "${datadir}/xsessions"
