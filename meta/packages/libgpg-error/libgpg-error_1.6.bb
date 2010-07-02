DESCRIPTION = "GPG-Error library"
SECTION = "libs"
LICENSE = "GPLv2+ & LGPLv2.1+"
DEPENDS = "gettext"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgpg-error/libgpg-error-${PV}.tar.bz2 \
           file://pkgconfig.patch;"

# move libgpg-error-config into -dev package
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir}/*"

inherit autotools binconfig pkgconfig
