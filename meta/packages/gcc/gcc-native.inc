DEPENDS = ""
PACKAGES = ""
PROVIDES = "gcc-native-${PV}"

inherit native

# This is intended to be a -very- basic config
EXTRA_OECONF = "--with-local-prefix=${STAGING_DIR_TARGET}${prefix} \
		--with-newlib \
		--disable-shared \
		--disable-threads \
		--disable-multilib \
		--disable-__cxa_atexit \
		--enable-languages=c \
		--enable-target-optspace \
		--program-prefix=${TARGET_PREFIX}"

do_install () {
	:
}

do_stage () {
	cd gcc
	oe_runmake install-common install-headers install-libgcc
	install -m 0755 xgcc ${STAGING_BINDIR}/gcc-${PV}
}
