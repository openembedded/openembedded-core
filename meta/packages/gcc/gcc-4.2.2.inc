require gcc-common.inc

DEPENDS = "mpfr gmp"

SRC_URI = "ftp://ftp.gnu.org/pub/gnu/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	file://100-uclibc-conf.patch;patch=1 \
	file://103-uclibc-conf-noupstream.patch;patch=1 \
	file://200-uclibc-locale.patch;patch=1 \
	file://203-uclibc-locale-no__x.patch;patch=1 \
	file://204-uclibc-locale-wchar_fix.patch;patch=1 \
	file://205-uclibc-locale-update.patch;patch=1 \
	file://300-libstdc++-pic.patch;patch=1 \
	file://301-missing-execinfo_h.patch;patch=1 \
	file://302-c99-snprintf.patch;patch=1 \
	file://303-c99-complex-ugly-hack.patch;patch=1 \
	file://304-index_macro.patch;patch=1 \
	file://305-libmudflap-susv3-legacy.patch;patch=1 \
	file://306-libstdc++-namespace.patch;patch=1 \
	file://307-locale_facets.patch;patch=1 \
	file://402-libbackend_dep_gcov-iov.h.patch;patch=1 \
	file://602-sdk-libstdc++-includes.patch;patch=1 \
	file://740-sh-pr24836.patch;patch=1 \
	file://800-arm-bigendian.patch;patch=1 \
	file://801-arm-bigendian-eabi.patch;patch=1 \
	file://904-flatten-switch-stmt-00.patch;patch=1 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
	file://gcc41-configure.in.patch;patch=1 \
	file://arm-thumb.patch;patch=1 \
	file://arm-thumb-cache.patch;patch=1 \
	file://ldflags.patch;patch=1 \
	file://zecke-xgcc-cpp.patch;patch=1 \
	file://unbreak-armv4t.patch;patch=1 \
	file://fix-ICE-in-arm_unwind_emit_set.diff;patch=1 \
	file://cache-amnesia.patch;patch=1 \
	file://gfortran.patch;patch=1 \
	file://gcc-4.0.2-e300c2c3.patch;patch=1 \
	file://pr34130.patch;patch=1 \
	file://fortran-static-linking.patch;patch=1 \
	file://intermask-bigendian.patch;patch=1 \
"

SRC_URI_append_ep93xx = " \
        file://arm-crunch-saveregs.patch;patch=1 \
        file://arm-crunch-20000320.patch;patch=1 \
        file://arm-crunch-compare.patch;patch=1 \
        file://arm-crunch-compare-unordered.patch;patch=1 \
        file://arm-crunch-compare-geu.patch;patch=1 \
        file://arm-crunch-eabi-ieee754.patch;patch=1 \
        file://arm-crunch-eabi-ieee754-div.patch;patch=1 \
        file://arm-crunch-64bit-disable0.patch;patch=1 \
        file://arm-crunch-offset.patch;patch=1 \
        file://arm-crunch-fp_consts.patch;patch=1 \
        file://arm-crunch-neg2.patch;patch=1 \
        file://arm-crunch-predicates3.patch;patch=1 \
        file://arm-crunch-cfcvtds-disable.patch;patch=1 \
        file://arm-crunch-floatsi-disable.patch;patch=1 \
        file://arm-crunch-truncsi-disable.patch;patch=1 \
        file://arm-crunch-cfcvt64-disable.patch;patch=1 \
        file://arm-crunch-cirrus-bugfixes.patch;patch=1 \
       "

PACKAGE_ARCH_ep93xx = "${MACHINE_ARCH}"

SRC_URI_append_sh3  = " file://sh3-installfix-fixheaders.patch;patch=1 "

# Language Overrides
FORTRAN = ""
FORTRAN_linux-gnueabi = ",fortran"
JAVA = ""

EXTRA_OECONF += " --disable-libssp --disable-bootstrap "

ARM_INSTRUCTION_SET = "arm"