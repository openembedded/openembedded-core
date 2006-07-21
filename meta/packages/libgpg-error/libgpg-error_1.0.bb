PR = "r0"
DESCRIPTION = "GPG-Error library"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL LGPL FDL"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgpg-error/libgpg-error-${PV}.tar.gz \
	   file://pkgconfig.patch;patch=1"

# move libgpg-error-config into -dev package
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir}"

inherit autotools binconfig pkgconfig

do_stage() {
	oe_libinstall -a -so -C src libgpg-error ${STAGING_LIBDIR}
	install -m 0755 src/gpg-error-config ${STAGING_BINDIR}/

	install -d ${STAGING_INCDIR}/
	for X in gpg-error.h
	do
		install -m 0644 ${S}/src/$X ${STAGING_INCDIR}/$X
	done

	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 src/gpg-error.m4 ${STAGING_DATADIR}/aclocal/
}
