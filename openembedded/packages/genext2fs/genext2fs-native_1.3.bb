include genext2fs_${PV}.bb
inherit native
FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/genext2fs-${PV}', '${FILE_DIRNAME}/genext2fs', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

do_stage () {
	install -m 0755 genext2fs ${STAGING_BINDIR}/
}

do_install () {
	:
}
