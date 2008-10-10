LICENSE = "unknown"
DESCRIPTION = "Tool to sign omap3 x-loader images"

inherit native 
SRC_URI = "file://signGP.c"

do_compile() {
	${CC} ${WORKDIR}/signGP.c -o signGP
}

do_stage() {
	install -d ${STAGING_BINDIR_NATIVE}
	install -m 0755 signGP ${STAGING_BINDIR_NATIVE}
}
