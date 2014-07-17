require gcc-common.inc

# Third digit in PV should be incremented after a minor release
# happens from this branch on gcc e.g. currently its 4.7.1
# when 4.7.2 is releases and we bump SRCREV beyond the release
# on branch then PV should be incremented to 4.7.2+svnr${SRCPV}
# to reflect that change

PV = "4.8.1"

# BINV should be incremented after updating to a revision
# after a minor gcc release (e.g. 4.7.1 or 4.7.2) has been made
# the value will be minor-release+1 e.g. if current minor release was
# 4.7.1 then the value below will have 2 which will mean 4.7.2
# which will be next minor release and so on.

BINV = "4.8.1"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/gcc-4.8' ], d)}"

DEPENDS =+ "mpfr gmp libmpc zlib"
NATIVEDEPS = "mpfr-native gmp-native libmpc-native zlib-native"

LICENSE="GPL-3.0-with-GCC-exception & GPLv3"

LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                   file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
                   file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
                   file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
		   file://COPYING.RUNTIME;md5=fe60d87048567d4fe8c8a0ed2448bcc8"

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://0001-gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
	   file://0002-uclibc-conf.patch \
	   file://0003-gcc-uclibc-locale-ctype_touplow_t.patch \
	   file://0004-uclibc-locale.patch \
	   file://0005-uclibc-locale-no__x.patch \
	   file://0006-uclibc-locale-wchar_fix.patch \
	   file://0007-uclibc-locale-update.patch \
	   file://0008-missing-execinfo_h.patch \
	   file://0009-c99-snprintf.patch \
	   file://0010-c99-complex-ugly-hack.patch \
	   file://0011-index_macro.patch \
	   file://0012-libmudflap-susv3-legacy.patch \
	   file://0013-libstdc-namespace.patch \
	   file://0014-sh-pr24836.patch \
	   file://0015-arm-Use-TARGET_ENDIAN_OPTION-for-determining-MULTILI.patch \
	   file://0016-gcc-poison-system-directories.patch \
	   file://0017-gcc-poison-dir-extend.patch \
	   file://0018-gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
	   file://0019-64-bit-multilib-hack.patch \
	   file://0020-optional-libstdc.patch \
	   file://0021-gcc-disable-MASK_RELAX_PIC_CALLS-bit.patch \
	   file://0022-COLLECT_GCC_OPTIONS.patch \
	   file://0023-Use-the-defaults.h-in-B-instead-of-S-and-t-oe-in-B.patch \
	   file://0024-PR-target-32219.patch \
	   file://0025-fortran-cross-compile-hack.patch \
	   file://0026-libgcc-sjlj-check.patch \
	   file://0027-cpp-honor-sysroot.patch \
	   file://0028-MIPS64-Default-to-N64-ABI.patch \
	   file://0029-Define-GLIBC_DYNAMIC_LINKER-and-UCLIBC_DYNAMIC_LINKE.patch \
	   file://0030-gcc-Fix-argument-list-too-long-error.patch \
	   file://0031-Disable-sdt.patch \
	   file://0032-libtool.patch \
	   file://0033-gcc-armv4-pass-fix-v4bx-to-linker-to-support-EABI.patch \
	   file://0034-Use-the-multilib-config-files-from-B-instead-of-usin.patch \
	   file://0035-wcast-qual-PR-55383.patch \
	   file://0036-PR-target-56102.patch \
	   file://0037-gcc-4.8-PR56797.patch \
	   file://0038-gcc-4.8-build-args.patch \
	   file://0039-gcc-4.8-PR57717.patch \
	   file://0040-fix-g++-sysroot.patch \
	   file://0041-libtool-avoid-libdir.patch \
	   file://0042-pr57748.patch \
	   file://0043-cpp.patch \
	   file://0044-gengtypes.patch \
	   file://0045-gcc-4.8-PR57717-PowerPC-E500v2.patch \
	   file://0046-libatomic-deptracking.patch \
	   file://0047-repomembug.patch \
	   file://0048-PR57532.patch \
	   file://0048-PR58854_fix_arm_apcs_epilogue.patch \
           file://0001-fix-ICE-when-building-opus.patch \
	  "
SRC_URI[md5sum] = "3b2386c114cd74185aa3754b58a79304"
SRC_URI[sha256sum] = "545b44be3ad9f2c4e90e6880f5c9d4f0a8f0e5f67e1ffb0d45da9fa01bb05813"

S = "${TMPDIR}/work-shared/gcc-${PV}-${PR}/gcc-${PV}"
B = "${WORKDIR}/gcc-${PV}/build.${HOST_SYS}.${TARGET_SYS}"

# Language Overrides
FORTRAN = ""
JAVA = ""

LTO = "--enable-lto"

EXTRA_OECONF_BASE = "   ${LTO} \
			--enable-libssp \
			--disable-bootstrap \
			--disable-libmudflap \
			--with-system-zlib \
			--with-linker-hash-style=${LINKER_HASH_STYLE} \
			--enable-linker-build-id \
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

