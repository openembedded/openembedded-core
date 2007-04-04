DESCRIPTION = "User interface for the OProfile tool"
SECTION = "x11"
LICENSE = "GPLv2"
PR = "r1"

inherit autotools pkgconfig

PACKAGES =+ "oprofileui-server oprofileui-viewer"

FILES_oprofileui-viewer = "${bindir}/oparchconv ${bindir}/oprofile-viewer ${datadir}/applications/ ${datadir}/oprofileui/"
FILES_oprofileui-server = "${bindir}/oprofile-server"
FILES_oprofileui-doc = "/usr/doc/oprofileui"
