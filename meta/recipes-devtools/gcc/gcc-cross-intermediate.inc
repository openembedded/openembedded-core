DEPENDS = "virtual/${TARGET_PREFIX}binutils ${NATIVEDEPS}"
DEPENDS += "virtual/${TARGET_PREFIX}libc-initial gettext-native"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-intermediate"
PACKAGES = ""

CROSS_TARGET_SYS_DIR_append = ".${PN}"

# This is intended to be a -very- basic config
# sysroot is needed in case we use libc-initial
#
# Glibc won't compile with gold, and building glibc is the whole point of
# this recipe.   So we select ld.bfd explicitly here if gold is the distro's 
# preferred linker.
EXTRA_OECONF = "--with-local-prefix=${STAGING_DIR_TARGET}${target_prefix} \
		--enable-shared \
		--disable-multilib \
		--disable-threads \
		--enable-languages=c \
                ${OPTSPACE} \
		--program-prefix=${TARGET_PREFIX} \
		--with-sysroot=${STAGING_DIR_TCBOOTSTRAP} \
		--with-build-sysroot=${STAGING_DIR_TCBOOTSTRAP} \
		${EXTRA_OECONF_INTERMEDIATE} \
		${@get_gcc_fpu_setting(bb, d)} \
		${@base_contains('DISTRO_FEATURES', 'ld-is-gold', '--with-ld=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}ld.bfd', '', d)}"

do_populate_sysroot[sstate-inputdirs] = "${SYSROOT_DESTDIR}/${STAGING_DIR_HOST} ${SYSROOT_DESTDIR}/${STAGING_DIR_TARGET}/${target_base_libdir}"
do_populate_sysroot[sstate-outputdirs] = "${STAGING_DIR_HOST} ${STAGING_DIR_TCBOOTSTRAP}/${target_base_libdir}"

do_compile () {
    oe_runmake
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${target_base_libdir}/
	mv ${D}${exec_prefix}/${TARGET_SYS}/${baselib}/* ${D}${target_base_libdir}/

	# We don't really need this (here shares/ contains man/, info/, locale/).
	rm -rf ${D}${datadir}/

	# We use libiberty from binutils
	find ${D}${exec_prefix}/lib -name libiberty.a | xargs rm -f
	find ${D}${exec_prefix}/lib -name libiberty.h | xargs rm -f

	# Insert symlinks into libexec so when tools without a prefix are searched for, the correct ones are
	# found. These need to be relative paths so they work in different locations.
	dest=${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/
	install -d $dest
	for t in ar as ld nm objcopy objdump ranlib strip g77 gcc cpp gfortran; do
		ln -sf ${BINRELPATH}/${TARGET_PREFIX}$t $dest$t
	done
}

#
# Override the default sysroot staging copy since this won't look like a target system
#
sysroot_stage_all() {
	sysroot_stage_dir ${D} ${SYSROOT_DESTDIR}
	install -d ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${target_base_libdir}/
	install -d ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${target_libdir}/  
	mv ${SYSROOT_DESTDIR}${target_base_libdir}/* ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${target_base_libdir}/ || true
	mv ${SYSROOT_DESTDIR}${target_libdir}/* ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${target_libdir}/ || true
}

