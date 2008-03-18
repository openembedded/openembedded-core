require gcc-common.inc

DEPENDS = "mpfr gmp"

SRC_URI = "ftp://ftp.gnu.org/pub/gnu/gcc/gcc-4.1.2/gcc-4.1.2.tar.bz2 \
	file://100-uclibc-conf.patch;patch=1 \
	file://110-arm-eabi.patch;patch=1 \
	file://200-uclibc-locale.patch;patch=1 \
	file://300-libstdc++-pic.patch;patch=1 \
	file://301-missing-execinfo_h.patch;patch=1 \
	file://302-c99-snprintf.patch;patch=1 \
	file://303-c99-complex-ugly-hack.patch;patch=1 \
	file://304-index_macro.patch;patch=1 \
	file://602-sdk-libstdc++-includes.patch;patch=1 \
	file://740-sh-pr24836.patch;patch=1 \
	file://800-arm-bigendian.patch;patch=1 \
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
       "

SRC_URI_append_sh3  = " file://sh3-installfix-fixheaders.patch;patch=1 "

SRC_URI_avr32 = "http://www.angstrom-distribution.org/unstable/sources/gcc-4.1.2-atmel.1.1.0.tar.gz \
#           file://100-uclibc-conf.patch;patch=1 \
#           file://200-uclibc-locale.patch;patch=1 \
#           file://300-libstdc++-pic.patch;patch=1 \
           file://301-missing-execinfo_h.patch;patch=1 \
           file://302-c99-snprintf.patch;patch=1 \
           file://303-c99-complex-ugly-hack.patch;patch=1 \
           file://304-index_macro.patch;patch=1 \
           file://602-sdk-libstdc++-includes.patch;patch=1 \
           file://gcc41-configure.in.patch;patch=1 \
           file://ldflags.patch;patch=1 \
           file://zecke-xgcc-cpp.patch;patch=1 \
           file://cache-amnesia.patch;patch=1 \
           "

do_compile_prepend_avr32() {
       ln -sf ${S}/libstdc++-v3/config/os/uclibc/ ${S}/libstdc++-v3/config/os/uclibc-linux
}


# Language Overrides
FORTRAN = ""
JAVA = ""

EXTRA_OECONF += " --disable-libssp "

ARM_INSTRUCTION_SET = "arm"
