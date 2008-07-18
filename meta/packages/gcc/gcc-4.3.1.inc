# No uclibc, armeb and thumb patches - really needs checking 
DEFAULT_PREFERENCE = "-99"

require gcc-common.inc

DEPENDS = "mpfr gmp"

SRC_URI = "ftp://ftp.gnu.org/pub/gnu/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	file://fedora/gcc43-c++-builtin-redecl.patch;patch=1;pnum=0 \
	file://fedora/gcc43-ia64-libunwind.patch;patch=1;pnum=0 \
	file://fedora/gcc43-java-nomulti.patch;patch=1;pnum=0 \
	file://fedora/gcc43-ppc32-retaddr.patch;patch=1;pnum=0 \
	file://fedora/gcc43-pr27898.patch;patch=1;pnum=0 \
	file://fedora/gcc43-pr32139.patch;patch=1;pnum=0 \
	file://fedora/gcc43-pr33763.patch;patch=1;pnum=0 \
	file://fedora/gcc43-rh330771.patch;patch=1;pnum=0 \
	file://fedora/gcc43-rh341221.patch;patch=1;pnum=0 \
	file://fedora/gcc43-cpp-pragma.patch;patch=1;pnum=0 \
	file://fedora/gcc43-java-debug-iface-type.patch;patch=1;pnum=0 \
	file://fedora/gcc43-libgomp-speedup.patch;patch=1;pnum=0 \
	file://fedora/gcc43-i386-libgomp.patch;patch=1;pnum=0 \
	file://fedora/gcc43-rh251682.patch;patch=1;pnum=0 \
        file://debian/arm-unbreak-eabi-armv4t.dpatch;patch=1 \
	file://debian/libstdc++-pic.dpatch;patch=1;pnum=0 \
	file://debian/gcc-ice-hack.dpatch;patch=1;pnum=0 \ 
	file://debian/pr30961.dpatch;patch=1;pnum=0 \
#	file://100-uclibc-conf.patch;patch=1 \
#	file://103-uclibc-conf-noupstream.patch;patch=1 \
#	file://200-uclibc-locale.patch;patch=1 \
#	file://203-uclibc-locale-no__x.patch;patch=1 \
#	file://204-uclibc-locale-wchar_fix.patch;patch=1 \
#	file://205-uclibc-locale-update.patch;patch=1 \
	file://301-missing-execinfo_h.patch;patch=1 \
#	file://302-c99-snprintf.patch;patch=1 \
#	file://303-c99-complex-ugly-hack.patch;patch=1 \
	file://304-index_macro.patch;patch=1 \
	file://305-libmudflap-susv3-legacy.patch;patch=1 \
#	file://306-libstdc++-namespace.patch;patch=1 \
#	file://307-locale_facets.patch;patch=1 \
#	file://402-libbackend_dep_gcov-iov.h.patch;patch=1 \
	file://602-sdk-libstdc++-includes.patch;patch=1 \
	file://740-sh-pr24836.patch;patch=1 \
#	file://800-arm-bigendian.patch;patch=1 \
#	file://801-arm-bigendian-eabi.patch;patch=1 \
	file://904-flatten-switch-stmt-00.patch;patch=1 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
#	file://gcc41-configure.in.patch;patch=1 \
#	file://arm-thumb.patch;patch=1 \
#	file://arm-thumb-cache.patch;patch=1 \
	file://ldflags.patch;patch=1 \
	file://zecke-xgcc-cpp.patch;patch=1 \
	file://fix-ICE-in-arm_unwind_emit_set.diff;patch=1 \
	file://cache-amnesia.patch;patch=1 \
	file://gfortran.patch;patch=1 \
#	file://gcc-4.0.2-e300c2c3.patch;patch=1 \
#	file://pr34130.patch;patch=1 \
#	file://fortran-static-linking.patch;patch=1 \
#	file://intermask-bigendian.patch;patch=1 \
	file://gcc-arm-frename-registers.patch;patch=1 \
	file://gcc-release-branch/PR-36500-fix-neon.patch;patch=1 \
"

SRC_URI_append_sh3  = " file://sh3-installfix-fixheaders.patch;patch=1 "

# Language Overrides
FORTRAN = ""
JAVA = ""

EXTRA_OECONF_BASE = "  --enable-cheaders=c_std --enable-libssp --disable-bootstrap --disable-libgomp --disable-libmudflap"

