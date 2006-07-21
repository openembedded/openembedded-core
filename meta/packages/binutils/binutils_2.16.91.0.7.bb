DESCRIPTION = "A GNU collection of binary utilities"
HOMEPAGE = "http://www.gnu.org/software/binutils/"
SECTION = "devel"
LICENSE = "GPL"
DEFAULT_PREFERENCE = "-1"

inherit autotools gettext

PACKAGES = "${PN} ${PN}-dev ${PN}-doc ${PN}-symlinks"

FILES_${PN} = " \
	${bindir}/${TARGET_PREFIX}* \
	${libdir}/lib*-*.so \
	${prefix}/${TARGET_SYS}/bin/*"

FILES_${PN}-dev = " \
	${includedir} \
	${libdir}/*.a \
	${libdir}/*.la \
	${libdir}/libbfd.so \
	${libdir}/libopcodes.so"

FILES_${PN}-symlinks = " \
	${bindir}/addr2line \
	${bindir}/ar \
	${bindir}/as \
	${bindir}/c++filt \
	${bindir}/gprof \
	${bindir}/ld \
	${bindir}/nm \
	${bindir}/objcopy \
	${bindir}/objdump \
	${bindir}/ranlib \
	${bindir}/readelf \
	${bindir}/size \
	${bindir}/strings \
	${bindir}/strip"

SRC_URI = \
    "http://ftp.kernel.org/pub/linux/devel/binutils/binutils-${PV}.tar.bz2 \
     file://binutils-2.16.91.0.6-objcopy-rename-errorcode.patch;patch=1 \
     file://binutils-uclibc-100-uclibc-conf.patch;patch=1 \
     file://binutils-uclibc-300-001_ld_makefile_patch.patch;patch=1 \
     file://binutils-uclibc-300-006_better_file_error.patch;patch=1 \
     file://binutils-uclibc-300-012_check_ldrunpath_length.patch;patch=1 \
     file://binutils-uclibc-400-mips-ELF_MAXPAGESIZE-4K.patch;patch=1 \
     file://binutils-uclibc-702-binutils-skip-comments.patch;patch=1"

S = "${WORKDIR}/binutils-${PV}"
B = "${S}/build.${HOST_SYS}.${TARGET_SYS}"

EXTRA_OECONF = "--program-prefix=${TARGET_PREFIX} \
		--enable-shared"

# This is necessary due to a bug in the binutils Makefiles
EXTRA_OEMAKE = "configure-build-libiberty all"

export AR = "${HOST_PREFIX}ar"
export AS = "${HOST_PREFIX}as"
export LD = "${HOST_PREFIX}ld"
export NM = "${HOST_PREFIX}nm"
export RANLIB = "${HOST_PREFIX}ranlib"
export OBJCOPY = "${HOST_PREFIX}objcopy"
export OBJDUMP = "${HOST_PREFIX}objdump"

export AR_FOR_TARGET = "${TARGET_PREFIX}ar"
export AS_FOR_TARGET = "${TARGET_PREFIX}as"
export LD_FOR_TARGET = "${TARGET_PREFIX}ld"
export NM_FOR_TARGET = "${TARGET_PREFIX}nm"
export RANLIB_FOR_TARGET = "${TARGET_PREFIX}ranlib"

export CC_FOR_HOST = "${CCACHE} ${HOST_PREFIX}gcc ${HOST_CC_ARCH}"
export CXX_FOR_HOST = "${CCACHE} ${HOST_PREFIX}gcc ${HOST_CC_ARCH}"

export CC_FOR_BUILD = "${BUILD_CC}"
export CPP_FOR_BUILD = "${BUILD_CPP}"
export CFLAGS_FOR_BUILD = "${BUILD_CFLAGS}"

export CC = "${CCACHE} ${HOST_PREFIX}gcc ${HOST_CC_ARCH}"

do_configure () {
	(cd ${S}; gnu-configize) || die "Failed to run gnu-configize"
	oe_runconf
}

do_stage () {
	oe_libinstall -so -a -C opcodes libopcodes ${STAGING_LIBDIR}/
	oe_libinstall -a -C libiberty libiberty ${STAGING_LIBDIR}/
	oe_libinstall -so -a -C bfd libbfd ${STAGING_LIBDIR}/
	install -m 0644 ${S}/include/dis-asm.h ${STAGING_INCDIR}/
	install -m 0644 ${S}/include/symcat.h ${STAGING_INCDIR}/
	install -m 0644 ${S}/include/libiberty.h ${STAGING_INCDIR}/
	install -m 0644 ${S}/include/ansidecl.h ${STAGING_INCDIR}/
	install -m 0644 ${S}/include/bfdlink.h ${STAGING_INCDIR}/
	install -m 0644 bfd/bfd.h ${STAGING_INCDIR}/
}

do_install () {
	autotools_do_install

	# We don't really need these, so we'll remove them...
	rm -rf ${D}${libdir}/ldscripts

	# Fix the /usr/${TARGET_SYS}/bin/* links
	for l in ${D}${prefix}/${TARGET_SYS}/bin/*; do
		rm -f $l
		ln -sf `echo ${prefix}/${TARGET_SYS}/bin \
			| tr -s / \
			| sed -e 's,^/,,' -e 's,[^/]*,..,g'`${bindir}/${TARGET_PREFIX}`basename $l` $l
	done

	# Install the libiberty header
	install -d ${D}${includedir}
	install -m 644 ${S}/include/ansidecl.h ${D}${includedir}
	install -m 644 ${S}/include/libiberty.h ${D}${includedir}

	cd ${D}${bindir}

	# Symlinks for ease of running these on the native target
	for p in ${TARGET_SYS}-* ; do
		ln -sf $p `echo $p | sed -e s,${TARGET_SYS}-,,`
	done
}
