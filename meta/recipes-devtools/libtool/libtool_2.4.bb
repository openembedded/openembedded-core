require libtool-${PV}.inc

PR = "r1"

#
# We want the results of libtool-cross preserved - don't stage anything ourselves.
#
SYSROOT_PREPROCESS_FUNCS += "libtool_sysroot_preprocess"

libtool_sysroot_preprocess () {
	rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${bindir}/*
	rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${datadir}/aclocal/*
	rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${datadir}/libtool/config/*
}

