SECTION = "devel"
inherit cross
DEPENDS += "flex-native bison-native gettext-native"
PROVIDES = "virtual/${TARGET_PREFIX}binutils"
PACKAGES = ""
EXTRA_OECONF = "--with-sysroot=${STAGING_DIR_TARGET} \
		--program-prefix=${TARGET_PREFIX} \
		--enable-install-libbfd \
                --disable-werror"

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# We don't really need these, so we'll remove them...
	rm -rf ${D}${STAGING_DIR_NATIVE}${prefix_native}/lib/ldscripts
	rm -rf ${D}${STAGING_DIR_NATIVE}${prefix_native}/share/info
	rm -rf ${D}${STAGING_DIR_NATIVE}${prefix_native}/share/locale
	rm -rf ${D}${STAGING_DIR_NATIVE}${prefix_native}/share/man
	rmdir ${D}${STAGING_DIR_NATIVE}${prefix_native}/share || :
	rmdir ${D}${STAGING_DIR_NATIVE}${prefix_native}/${libdir}/gcc-lib || :
	rmdir ${D}${STAGING_DIR_NATIVE}${prefix_native}/${libdir}64/gcc-lib || :
	rmdir ${D}${STAGING_DIR_NATIVE}${prefix_native}/${libdir} || :
	rmdir ${D}${STAGING_DIR_NATIVE}${prefix_native}/${libdir}64 || :
	rmdir ${D}${STAGING_DIR_NATIVE}${prefix_native}/${prefix} || :
}
