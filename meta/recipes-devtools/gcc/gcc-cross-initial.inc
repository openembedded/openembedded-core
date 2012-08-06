DEPENDS = "virtual/${TARGET_PREFIX}binutils ${NATIVEDEPS}"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-initial"
PACKAGES = ""

INHIBIT_AUTOTOOLS_DEPS = "1"
INHIBIT_DEFAULT_DEPS = "1"

CROSS_TARGET_SYS_DIR_append = ".${PN}"

# This is intended to be a -very- basic config
# sysroot is needed in case we use libc-initial
EXTRA_OECONF = "--with-newlib \
		--without-headers \
		--disable-shared \
		--disable-threads \
		--disable-multilib \
		--disable-__cxa_atexit \
		--enable-languages=c \
                ${OPTSPACE} \
		--program-prefix=${TARGET_PREFIX} \
		--with-sysroot=${STAGING_DIR_TARGET} \
		--with-build-sysroot=${GCCCROSS_BUILDSYSROOT} \
		${EXTRA_OECONF_INITIAL} \
		${@base_contains('DISTRO_FEATURES', 'ld-is-gold', '--with-ld=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}ld.bfd', '', d)} \
		${EXTRA_OECONF_FPU}"


GCCCROSS_BUILDSYSROOT = "${B}/tmpsysroot"

do_configure_prepend () {
	sysr=${GCCCROSS_BUILDSYSROOT}${target_includedir}
	mkdir -p $sysr
	for t in linux asm asm-generic; do
		rm -f $sysr/$t
		ln -s ${STAGING_DIR_TARGET}${target_includedir}/$t $sysr/
	done
}

do_compile () {
    oe_runmake all-gcc all-target-libgcc
}
# fixed limits.h infact includes the so called real limits.h
# which should come from libc but when we build libc-initial
# then bunch of configure tests include fixed limits.h which in turn
# includes real limits.h but this real limits.h is not staged yet
# so we overwirte the generated include-fixed/limits.h for gcc-cross-initial
# to get rid references to real limits.h

do_install_append () {
    cp gcc/include-fixed/limits.h ${D}${gcclibdir}/${TARGET_SYS}/${BINV}/include-fixed/limits.h
}
