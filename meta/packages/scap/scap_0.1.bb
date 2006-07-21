LICENSE = "OSL"
PR = "r4"

DESCRIPTION = "handhelds.org screen capture utility"
SRC_URI = "file://scap.sh file://scap.desktop file://scap.png"

do_install() {
	install -d ${D}${bindir}
	install ${WORKDIR}/scap.sh ${D}${bindir}/scap
	install -d ${D}${datadir}/applications
	install -m 0644 ${WORKDIR}/scap.desktop ${D}${datadir}/applications/
	install -d ${D}${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/scap.png ${D}${datadir}/pixmaps/
}

