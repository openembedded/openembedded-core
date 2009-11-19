DESCRIPTION = "The Python Programming Language"
HOMEPAGE = "http://www.python.org"
LICENSE = "PSF"
DEPENDS = "openssl-native bzip2-full-native sqlite3-native"
SECTION = "devel/python"
PRIORITY = "optional"
PR = "ml6"

EXCLUDE_FROM_WORLD = "1"

SRC_URI = "\
  http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.bz2 \
  file://bindir-libdir.patch;patch=1 \
  file://cross-distutils.patch;patch=1 \
  file://dont-modify-shebang-line.patch;patch=1 \
  file://default-is-optimized.patch;patch=1 \
  file://catchup-with-swig.patch;patch=1 \
  file://fix-staging.patch;patch=1 \
  file://enable-ctypes-module.patch;patch=1 \
"
S = "${WORKDIR}/Python-${PV}"

inherit autotools native

EXTRA_OECONF = "--with-threads --with-pymalloc --with-cyclic-gc \
                --without-cxx --with-signal-module --with-wctype-functions"
EXTRA_OEMAKE = 'BUILD_SYS="" HOST_SYS="" STAGING_LIBDIR=${STAGING_LIBDIR} \
		STAGING_INCDIR=${STAGING_INCDIR}'

do_install() {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${bindir}/
	install -m 0755 Parser/pgen ${D}${bindir}/
}
