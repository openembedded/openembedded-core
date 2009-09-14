do_stage() {
	# Installing directly into staging creates some races between existing
	# glibc-initial files and us. We could use a new DESTDIR but we may as
	# well just copy the data installed by do_install
	cp -pPR ${D}/* ${STAGING_DIR_HOST}/
}

