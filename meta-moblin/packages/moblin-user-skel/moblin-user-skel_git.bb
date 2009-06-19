SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

do_compile () {
	:
}

do_install () {
	install -d ${D}${sysconfdir}/skel/.bkl-thumbnails/
	cp -pPR ${S}/.bkl-thumbnails/* ${D}${sysconfdir}/skel/.bkl-thumbnails/

	install -d ${D}${sysconfdir}/skel/.kozo/
	cp -pPR ${S}/.kozo/* ${D}${sysconfdir}/skel/.kozo/

	install -d ${D}${sysconfdir}/skel/.local/
	cp -pPR ${S}/.local/* ${D}${sysconfdir}/skel/.local/
}
