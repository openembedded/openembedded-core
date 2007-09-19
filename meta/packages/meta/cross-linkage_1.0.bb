DESCRIPTION = "cross-linkage sets up symlinks between cross and staging so the compiler can find things"
SECTION = "devel"
PACKAGES = ""

INHIBIT_DEFAULT_DEPS = "1"
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
	install -d ${CROSS_DIR}/${TARGET_SYS}/
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include
	ln -s  ${STAGING_INCDIR}/ ${CROSS_DIR}/${TARGET_SYS}/include
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/lib
	ln -s  ${STAGING_LIBDIR} ${CROSS_DIR}/${TARGET_SYS}/lib 
}
