require libtool-${PV}.inc

PR = "${INC_PR}.0"

#
# We want the results of libtool-cross preserved - don't stage anything ourselves.
#
SYSROOT_PREPROCESS_FUNCS += "libtool_sysroot_preprocess"

libtool_sysroot_preprocess () {
	rm -rf ${SYSROOT_DESTDIR}${bindir}/*
	rm -rf ${SYSROOT_DESTDIR}${datadir}/aclocal/*
	rm -rf ${SYSROOT_DESTDIR}${datadir}/libtool/config/*
}

