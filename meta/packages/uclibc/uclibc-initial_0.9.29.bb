SECTION = "base"
require uclibc_${PV}.bb

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/uclibc-${PV}', '${FILE_DIRNAME}/uclibc', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

DEPENDS = "linux-libc-headers"
PROVIDES = "virtual/${TARGET_PREFIX}libc-initial"
PACKAGES = ""

do_stage() {
	# Install initial headers into the cross dir
	make PREFIX= DEVEL_PREFIX=${UCLIBC_STAGE_PREFIX}/ \
		RUNTIME_PREFIX=${UCLIBC_STAGE_PREFIX}/ \
		pregen install_dev

	ln -sf include ${CROSS_DIR}/${TARGET_SYS}/sys-include

	# This conflicts with the c++ version of this header
	rm -f ${UCLIBC_STAGE_PREFIX}/include/bits/atomicity.h
}

do_install() {
	:
}

do_compile () {
	:
}
