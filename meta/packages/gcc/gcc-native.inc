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

NATIVE_INSTALL_WORKS = "1"
do_install () {
	cd gcc
	oe_runmake 'DESTDIR=${D}' install-common install-headers install-libgcc
	install -d ${D}${bindir}/
	install -m 0755 xgcc ${D}${bindir}/gcc-${PV}
}
