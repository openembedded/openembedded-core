require gcc-common.inc

PR = "r13"

# Third digit in PV should be incremented after a minor release
# happens from this branch on gcc e.g. currently its 4.7.1
# when 4.7.2 is releases and we bump SRCREV beyond the release
# on branch then PV should be incremented to 4.7.2+svnr${SRCPV}
# to reflect that change

PV = "4.7.2"

# BINV should be incremented after updating to a revision
# after a minor gcc release (e.g. 4.7.1 or 4.7.2) has been made
# the value will be minor-release+1 e.g. if current minor release was
# 4.7.1 then the value below will have 2 which will mean 4.7.2
# which will be next minor release and so on.

BINV = "4.7.2"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/gcc-4.7' ], d)}"

DEPENDS =+ "mpfr gmp libmpc"
NATIVEDEPS = "mpfr-native gmp-native libmpc-native zlib-native"

LICENSE="GPL-3.0-with-GCC-exception & GPLv3"

LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                   file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
                   file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
                   file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
		   file://COPYING.RUNTIME;md5=fe60d87048567d4fe8c8a0ed2448bcc8"

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
	   file://100-uclibc-conf.patch \
           file://gcc-uclibc-locale-ctype_touplow_t.patch \
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
           file://gcc-poison-system-directories.patch \
           file://gcc-poison-dir-extend.patch \
           file://gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
	   file://64bithack.patch \
	   file://optional_libstdc.patch \
	   file://disable_relax_pic_calls_flag.patch \
	   file://COLLECT_GCC_OPTIONS.patch \
           file://use-defaults.h-and-t-oe-in-B.patch \
	   file://ppc_with_cpu.patch \
	   file://ppce5500-e6500-support.patch \
	   file://rs6000-tables.patch \
	   file://define_insn_reservation.patch \
	   file://pr32219.patch \
	   file://fortran-cross-compile-hack.patch \
	   file://libgcc-sjlj-check.patch \
	   file://cpp-honor-sysroot.patch \
	   file://mips64-default-n64.patch \
	   file://GLIBC_DYNAMIC_LINKER.patch \
	   file://arm-hard-float-loader.patch \
	   file://gcc-argument-list-too-long.patch \
	   file://fix-g++-sysroot.patch \
	   file://disablesdt.patch \
	   file://libtool.patch \
	   file://gcc-armv4-pass-fix-v4bx-to-ld.patch \
	   file://ppc_no_crtsavres.patch \
	   file://0001-crtstuff.c-USE_PT_GNU_EH_FRAME-Define-for-systems-us.patch \
	   file://0001-Makefile.in-vis_hide-gen-hide-list-Do-not-make-defin.patch \
	   file://disable-texinfo-5.patch \
	  "
SRC_URI[md5sum] = "cc308a0891e778cfda7a151ab8a6e762"
SRC_URI[sha256sum] = "8a9283d7010fb9fe5ece3ca507e0af5c19412626384f8a5e9434251ae100b084"

S = "${TMPDIR}/work-shared/gcc-${PV}-${PR}/gcc-${PV}"
B = "${WORKDIR}/gcc-${PV}/build.${HOST_SYS}.${TARGET_SYS}"

# Language Overrides
FORTRAN = ""
JAVA = ""

EXTRA_OECONF_BASE = " --enable-lto \
			--enable-libssp \
			--disable-bootstrap \
			--disable-libgomp \
			--disable-libmudflap \
			--with-system-zlib \
			--with-linker-hash-style=${LINKER_HASH_STYLE} \
			--with-ppl=no \
			--with-cloog=no \
			--enable-checking=release \
			--enable-cheaders=c_global "

EXTRA_OECONF_INITIAL = "--disable-libmudflap \
			--disable-libgomp \
			--disable-libssp \
			--disable-libquadmath \
			--with-system-zlib \
			--disable-lto \
			--disable-plugin \
			--enable-decimal-float=no"

EXTRA_OECONF_INTERMEDIATE = "--disable-libmudflap \
				--disable-libgomp \
				--disable-libquadmath \
				--with-system-zlib \
				--disable-lto \
				--disable-plugin \
				--disable-libssp"

EXTRA_OECONF_append_libc-uclibc = " --disable-decimal-float "

EXTRA_OECONF_PATHS = " \ 
                      --with-gxx-include-dir=${STAGING_DIR_TARGET}${target_includedir}/c++ \ 
                      --with-sysroot=${STAGING_DIR_TARGET} \ 
                      --with-build-sysroot=${STAGING_DIR_TARGET}"

do_configure_prepend () {
        # teach gcc to find correct target includedir when checking libc ssp support
        mkdir -p ${B}/gcc
        echo "NATIVE_SYSTEM_HEADER_DIR = ${SYSTEMHEADERS}" > ${B}/gcc/t-oe
        cat ${S}/gcc/defaults.h | grep -v "\#endif.*GCC_DEFAULTS_H" > ${B}/gcc/defaults.h.new
        cat >>${B}/gcc/defaults.h.new <<_EOF
#ifndef STANDARD_STARTFILE_PREFIX_1
#define STANDARD_STARTFILE_PREFIX_1 "${SYSTEMLIBS}"
#endif
#ifndef STANDARD_STARTFILE_PREFIX_2
#define STANDARD_STARTFILE_PREFIX_2 "${SYSTEMLIBS1}"
#endif
#define SYSTEMLIBS_DIR "${SYSTEMLIBS}"
#endif /* ! GCC_DEFAULTS_H */
_EOF
        mv ${B}/gcc/defaults.h.new ${B}/gcc/defaults.h
}

