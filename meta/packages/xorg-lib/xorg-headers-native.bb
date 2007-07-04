inherit native

SRC_URI = "file://X11"

do_stage() {
	install -d ${STAGING_INCDIR}
	cp -pPfR ${WORKDIR}/X11 ${STAGING_INCDIR}
}
