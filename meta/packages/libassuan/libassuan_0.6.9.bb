LICENSE = "GPL"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/alpha/libassuan/libassuan-${PV}.tar.gz"

inherit autotools binconfig

do_stage() {
	autotools_stage_includes

	install -d ${STAGING_LIBDIR}
	oe_libinstall -C src -a libassuan ${STAGING_LIBDIR}

	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 src/libassuan.m4 ${STAGING_DATADIR}/aclocal/
}

