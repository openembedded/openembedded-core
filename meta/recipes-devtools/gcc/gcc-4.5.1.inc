require gcc-common.inc

PR = "r12"

DEPENDS =+ "mpfr gmp libmpc elfutils"
NATIVEDEPS = "mpfr-native gmp-native gettext-native libmpc-native elfutils-native"

LICENSE="GPL-3.0-with-GCC-exception & GPLv2 & GPLv3 & LGPLv2.1 & LGPLv3"

LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                   file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
                   file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
                   file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
		   file://COPYING.RUNTIME;md5=fe60d87048567d4fe8c8a0ed2448bcc8"

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
	   file://100-uclibc-conf.patch \
           file://gcc-uclibc-locale-ctype_touplow_t.patch \
	   file://cache-amnesia.patch \
	   file://gcc-flags-for-build.patch \
	   file://libstdc++-emit-__cxa_end_cleanup-in-text.patch \
	   file://arm-bswapsi2.patch \
		\
           file://fedora/gcc43-c++-builtin-redecl.patch;striplevel=0 \
           file://fedora/gcc43-ia64-libunwind.patch;striplevel=0 \
           file://fedora/gcc43-java-nomulti.patch;striplevel=0 \
           file://fedora/gcc43-ppc32-retaddr.patch;striplevel=0 \
           file://fedora/gcc43-pr32139.patch;striplevel=0 \
           file://fedora/gcc43-pr33763.patch;striplevel=0 \
           file://fedora/gcc43-rh330771.patch;striplevel=0 \
           file://fedora/gcc43-rh341221.patch;striplevel=0 \
           file://fedora/gcc43-java-debug-iface-type.patch;striplevel=0 \
           file://fedora/gcc43-i386-libgomp.patch;striplevel=0 \
           file://fedora/gcc45-no-add-needed.patch;striplevel=0 \
           file://103-uclibc-conf-noupstream.patch  \
           file://200-uclibc-locale.patch  \
           file://203-uclibc-locale-no__x.patch; \
           file://204-uclibc-locale-wchar_fix.patch; \
           file://205-uclibc-locale-update.patch; \
           file://301-missing-execinfo_h.patch  \
           file://302-c99-snprintf.patch  \
           file://303-c99-complex-ugly-hack.patch  \
           file://304-index_macro.patch  \
           file://305-libmudflap-susv3-legacy.patch  \
           file://306-libstdc++-namespace.patch  \
           file://740-sh-pr24836.patch  \
           file://800-arm-bigendian.patch  \
           file://904-flatten-switch-stmt-00.patch  \
           file://arm-nolibfloat.patch  \
           file://arm-softfloat.patch \
           file://zecke-xgcc-cpp.patch \
           file://gcc-poison-system-directories.patch \
           file://gcc-poison-dir-extend.patch \
           file://gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
	   file://64bithack.patch \
	   file://optional_libstdc.patch \
	   file://disable_relax_pic_calls_flag.patch \
	   file://GPLUSPLUS_INCLUDE_DIR_with_sysroot.patch \
	   file://COLLECT_GCC_OPTIONS.patch \
	   file://use-defaults.h-and-t-oe-in-B.patch \
	   file://pr43810.patch \
	   file://pr44290.patch \
	   file://pr44606.patch \
	   file://pr45094.patch \
	   file://pr45052.patch \
	   file://pr45886.patch \
	   file://pr44618.patch \
	  "
	
SRC_URI_append_sh3  = " file://sh3-installfix-fixheaders.patch "

# Language Overrides
FORTRAN = ""
JAVA = ""

#EXTRA_OECONF_BASE = "  --enable-cheaders=c_std \
#			--enable-libssp \
#			--disable-bootstrap \
#			--disable-libgomp \
#			--disable-libmudflap"
EXTRA_OECONF_BASE = " --enable-lto \
			--enable-libssp \
			--disable-bootstrap \
			--disable-libgomp \
			--disable-libmudflap \
			--enable-cheaders=c_global "

EXTRA_OECONF_INITIAL = "--disable-libmudflap \
			--disable-libgomp \
			--disable-libssp \
			--enable-decimal-float=no"

EXTRA_OECONF_INTERMEDIATE = "--disable-libmudflap \
				--disable-libgomp \
				--disable-libssp"

EXTRA_OECONF_append_linux-uclibc = " --disable-decimal-float "
EXTRA_OECONF_append_linux-uclibceabi = " --disable-decimal-float "
EXTRA_OECONF_append_linux-uclibcspe = " --disable-decimal-float "
