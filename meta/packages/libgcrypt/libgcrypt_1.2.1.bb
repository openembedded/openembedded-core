DESCRIPTION = "A general purpose cryptographic library based on the code from GnuPG"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL LGPL FDL"
DEPENDS = "libgpg-error"

# move libgcrypt-config into -dev package
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir}"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgcrypt/libgcrypt-${PV}.tar.gz"

inherit autotools binconfig

EXTRA_OECONF = "--without-pth --disable-asm --with-capabilities"

do_stage() {
	oe_libinstall -so -C src libgcrypt ${STAGING_LIBDIR}
	oe_libinstall -so -C src libgcrypt-pthread ${STAGING_LIBDIR}
	install -m 0755 src/libgcrypt-config ${STAGING_BINDIR}/

	install -d ${STAGING_INCDIR}/
	for X in gcrypt.h gcrypt-module.h
	do
		install -m 0644 src/${X} ${STAGING_INCDIR}/${X}
	done

}
