DESCRIPTION = "cross-linkage sets up symlinks between cross and staging so the compiler can find things"
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
	install -d ${CROSS_DIR}/${TARGET_SYS}/
	if [ -e ${CROSS_DIR}/${TARGET_SYS}/include ]; then
		cp -pPRr ${CROSS_DIR}/${TARGET_SYS}/include/* ${STAGING_INCDIR}
		mv ${CROSS_DIR}/${TARGET_SYS}/include/ ${CROSS_DIR}/${TARGET_SYS}/include-oldbackup
	fi
	ln -s  ${STAGING_INCDIR}/ ${CROSS_DIR}/${TARGET_SYS}/include
	if [ -e ${CROSS_DIR}/${TARGET_SYS}/lib ]; then
		cp -pPRr ${CROSS_DIR}/${TARGET_SYS}/lib/* ${STAGING_LIBDIR}
		mv ${CROSS_DIR}/${TARGET_SYS}/lib/ ${CROSS_DIR}/${TARGET_SYS}/lib-oldbackup
	fi
	ln -s  ${STAGING_LIBDIR} ${CROSS_DIR}/${TARGET_SYS}/lib 
}
