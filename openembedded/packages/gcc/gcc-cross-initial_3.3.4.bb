SECTION = "devel"
include gcc-cross_${PV}.bb

DEPENDS = "virtual/${TARGET_PREFIX}binutils"
DEPENDS += "${@['virtual/${TARGET_PREFIX}libc-initial',''][bb.data.getVar('TARGET_ARCH', d, 1) in ['arm', 'armeb', 'mips', 'mipsel']]}"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-initial"

# This is intended to be a -very- basic config
EXTRA_OECONF = "--with-local-prefix=${CROSS_DIR}/${TARGET_SYS} \
		--with-newlib \
		--disable-shared \
		--disable-threads \
		--disable-multilib \
		--disable-__cxa_atexit \
		--enable-languages=c \
		--enable-target-optspace \
		--program-prefix=${TARGET_PREFIX} \
		${@get_gcc_fpu_setting(bb, d)}"

do_stage_prepend () {
	mkdir -p ${CROSS_DIR}/lib/gcc-lib/${TARGET_SYS}/${PV}
	ln -sf libgcc.a ${CROSS_DIR}/lib/gcc-lib/${TARGET_SYS}/${PV}/libgcc_eh.a
}

# Override the method from gcc-cross so we don't try to install libgcc
do_install () {
	oe_runmake 'DESTDIR=${D}' install
}
