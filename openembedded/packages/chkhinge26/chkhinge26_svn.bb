LICENSE = "LGPL"
SECTION = "x11"
MAINTAINER = "Matthew Allum <mallum@openedhand.com>"
DESCRIPTION = "Chkhinge26 fires off cmds on cXXXX Zs."
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=chkhinge26;proto=http \
	   file://hinge-handler"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig

do_install_append () {
	install -m 0755 ${WORKDIR}/hinge-handler ${D}/${bindir}/
}

FILES_${PN} += "${bindir}/hinge-handler"


