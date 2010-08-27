require gcc-common.inc
LICENSE = "GPLv3"

DEPENDS =+ "mpfr gmp libmpc elfutils"
NATIVEDEPS = "mpfr-native gmp-native gettext-native libmpc-native elfutils-native"

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://gcc-4.5.0_to_svn_162697.patch.bz2 \
	   file://gcc_revert_base_version_to_4.5.0.patch \
	   file://gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
	   file://100-uclibc-conf.patch \
           file://gcc-uclibc-locale-ctype_touplow_t.patch \
	   file://cache-amnesia.patch \
	   file://gcc-flags-for-build.patch \
	   file://libstdc++-emit-__cxa_end_cleanup-in-text.patch \
	   file://arm-bswapsi2.patch \
	   file://gcc-pr43698-arm-rev-instr.patch \
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
           file://103-uclibc-conf-noupstream.patch  \
           file://200-uclibc-locale.patch  \
           file://203-uclibc-locale-no__x.patch;striplevel=1 \
           file://204-uclibc-locale-wchar_fix.patch;striplevel=1 \
           file://205-uclibc-locale-update.patch;striplevel=1 \
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
           file://gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
	   file://64bithack.patch \
	   file://optional_libstdc.patch \
	  "
	
SRC_URI_append_sh3  = " file://sh3-installfix-fixheaders.patch;patch=1 "

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
