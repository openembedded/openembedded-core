SECTION = "base"
require uclibc_${PV}.bb

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/uclibc-${PV}', '${FILE_DIRNAME}/uclibc', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

DEPENDS = "linux-libc-headers"
PROVIDES = "virtual/${TARGET_PREFIX}libc-initial"
PACKAGES = ""

do_stage() {
	# Install initial headers into the cross dir
	make PREFIX= DEVEL_PREFIX=${UCLIBC_PREFIX}/ \
		RUNTIME_PREFIX=${UCLIBC_PREFIX}/ \
		pregen install_dev

	mkdir -p ${CROSS_DIR}/${TARGET_SYS}
	mkdir -p ${CROSS_DIR}/${TARGET_SYS}/include
	mkdir -p ${CROSS_DIR}/${TARGET_SYS}/lib
	ln -sf include ${CROSS_DIR}/${TARGET_SYS}/sys-include
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/${prefix}

	# This conflicts with the c++ version of this header
	rm -f ${UCLIBC_PREFIX}/include/bits/atomicity.h
}

do_install() {
	:
}

do_compile () {
	:
}
