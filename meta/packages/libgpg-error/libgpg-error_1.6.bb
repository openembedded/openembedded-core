DESCRIPTION = "GPG-Error library"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL LGPL FDL"
DEPENDS = "gettext"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgpg-error/libgpg-error-${PV}.tar.bz2 \
	   file://pkgconfig.patch;patch=1"

# move libgpg-error-config into -dev package
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir}/*"

inherit autotools_stage binconfig pkgconfig
