DESCRIPTION = "staging-linkage sets up symlinks in staging so old compilers continue to work with the sysroot staging layout changes"
SECTION = "devel"
PACKAGES = ""

INHIBIT_DEFAULT_DEPS = "1"
EXCLUDE_FROM_WORLD = "1"
PR = "r0"

SRC_URI = ""

do_configure() {
	:
}

do_compile () {
	:
}

do_install() {
	:
}

do_stage () {
	if [ -e ${STAGING_DIR_HOST}${layout_base_libdir} ]; then
		cp -pPRr ${STAGING_DIR_HOST}${layout_base_libdir}/* ${STAGING_LIBDIR}
		mv ${STAGING_DIR_HOST}${layout_base_libdir}/ ${STAGING_DIR_HOST}${layout_libdir}-oldbackup
	fi
	ln -s ${STAGING_LIBDIR}/ ${STAGING_DIR_HOST}${layout_base_libdir}
}
