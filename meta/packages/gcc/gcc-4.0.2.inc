require gcc-common.inc

DEPENDS = "mpfr gmp"

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
	file://ldflags.patch;patch=1 \
	file://GCOV_PREFIX_STRIP-cross-profile_4.1.patch;patch=1 \
	file://zecke-xgcc-cpp.patch;patch=1 "

# uclibc patches below
SRC_URI_append = " file://100-uclibc-conf.patch;patch=1   \
                   file://200-uclibc-locale.patch;patch=1 \
                   file://301-missing-execinfo_h.patch;patch=1 \
                   file://302-c99-snprintf.patch;patch=1  \
                   file://303-c99-complex-ugly-hack.patch;patch=1 \
                   file://800-arm-bigendian.patch;patch=1 \
                   file://zecke-host-cpp-ac-hack.patch;patch=1 \
		   file://gcc-4.0.2-atmel.0.99.2.patch;patch=1 \
		   "

SRC_URI_append_fail-fast = " file://zecke-no-host-includes.patch;patch=1 "

# Language Overrides
FORTRAN = ""

EXTRA_OECONF += "--disable-libssp"

