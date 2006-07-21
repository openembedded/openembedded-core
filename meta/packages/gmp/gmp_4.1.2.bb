SECTION = "libs"
DESCRIPTION = "GNU multiprecision arithmetic library"
HOMEPAGE = "http://www.swox.com/gmp/"
LICENSE = "GPL LGPL"

SRC_URI = "ftp://mirrors.kernel.org/gnu/gmp/gmp-${PV}.tar.gz \
	   file://configure.patch;patch=1 \
	   file://amd64.patch;patch=1 \
           file://gcc-compile.patch;patch=1 "

inherit autotools 

acpaths = ""

do_stage () {
	oe_libinstall -so libgmp ${STAGING_LIBDIR}
	install -m 0644 ${S}/gmp.h ${STAGING_INCDIR}/gmp.h
}
