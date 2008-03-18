DEPENDS = "virtual/${TARGET_PREFIX}binutils"
# @todo Please add comment on why this is (still?) needed?
DEPENDS += "${@['virtual/${TARGET_PREFIX}libc-initial',''][bb.data.getVar('TARGET_ARCH', d, 1) in ['arm', 'armeb', 'mips', 'mipsel', 'powerpc']]}"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-initial"
PACKAGES = ""

# This is intended to be a -very- basic config
# sysroot is needed in case we use libc-initial
EXTRA_OECONF = "--with-local-prefix=${STAGING_DIR_TARGET}${layout_prefix} \
		--with-newlib \
		--disable-shared \
		--disable-threads \
		--disable-multilib \
		--disable-__cxa_atexit \
		--enable-languages=c \
		--enable-target-optspace \
		--program-prefix=${TARGET_PREFIX} \
		--with-sysroot=${STAGING_DIR_TARGET} \
		--with-build-sysroot=${STAGING_DIR_TARGET} \
		${@get_gcc_fpu_setting(bb, d)}"

do_stage_prepend () {
	mkdir -p ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}
	ln -sf libgcc.a ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/libgcc_eh.a
}

do_install () {
	:
}
