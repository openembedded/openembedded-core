SECTION = "devel"
PR = "r2"
inherit autotools gettext

DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
LICENSE = "GPL"
MAINTAINER = "Gerald Britton <gbritton@doomcom.org>"

# libgcc libstdc++ libg2c are listed in our FILES_*, but are actually
# packaged in the respective cross packages.
PACKAGES = "${PN} ${PN}-symlinks \
            ${PN}-c++ ${PN}-c++-symlinks \
            ${PN}-f77 ${PN}-f77-symlinks \
            libstdc++-dev libg2c-dev \
            ${PN}-doc"

FILES_${PN} = "${bindir}/${TARGET_PREFIX}gcc \
	       ${bindir}/${TARGET_PREFIX}cpp \
	       ${bindir}/${TARGET_PREFIX}gcov \
	       ${bindir}/${TARGET_PREFIX}gccbug \
	       ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/cc1 \
	       ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/collect2 \
	       ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/crt* \
	       ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/specs \
	       ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/lib* \
	       ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/include"

FILES_${PN}-symlinks = "${bindir}/cc \
			${bindir}/gcc \
			${bindir}/cpp \
			${bindir}/gcov \
			${bindir}/gccbug"

FILES_${PN}-c++ = "${bindir}/${TARGET_PREFIX}g++ \
		   ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/cc1plus"

FILES_${PN}-c++-symlinks = "${bindir}/c++ \
			    ${bindir}/g++"

PACKAGE_ARCH_libgcc = "${TARGET_ARCH}"
FILES_libgcc = "/lib/libgcc_s.so.*"

PACKAGE_ARCH_libstdc++ = "${TARGET_ARCH}"
PACKAGE_ARCH_libstdc++-dev = "${TARGET_ARCH}"
# Called from within gcc-cross, so libdir is set wrong
#FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++ = "${libdir}/libstdc++.so.*"

FILES_libstdc++-dev = "${includedir}/c++/${PV} \
		       ${libdir}/libstdc++.so \
		       ${libdir}/libstdc++.la \
		       ${libdir}/libstdc++.a \
		       ${libdir}/libsupc++.la \
		       ${libdir}/libsupc++.a"

FILES_${PN}-doc = "${infodir} \
		   ${mandir} \
		   ${libdir}/gcc-lib/${TARGET_SYS}/${PV}/include/README"

SRC_URI = "${GNU_MIRROR}/gcc/releases/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://arm-gotoff.dpatch;patch=1;pnum=0 \
	   file://arm-ldm.dpatch;patch=1;pnum=0 \
	   file://arm-tune.patch;patch=1;pnum=0 \
	   file://arm-ldm-peephole.patch;patch=1;pnum=0 \
	   file://libibery-crosstool.patch;patch=1;pnum=1 \
	   file://reverse-compare.patch;patch=1 \
	   file://gcc34-15089.patch;patch=1 \
	   file://gcc-uclibc-3.3-100-conf.patch;patch=1 \
	   file://gcc-uclibc-3.3-110-conf.patch;patch=1 \
	   file://gcc-uclibc-3.3-120-softfloat.patch;patch=1 \
	   file://gcc-uclibc-3.3-200-code.patch;patch=1 \
	   file://bash3.patch;patch=1"

PREMIRRORS_prepend () {
${GNU_MIRROR}/gcc/releases/	ftp://gcc.gnu.org/pub/gcc/releases/
${GNU_MIRROR}/gcc/releases/	http://gcc.get-software.com/releases/
}

S = "${WORKDIR}/gcc-${PV}"
B = "${S}/build.${HOST_SYS}.${TARGET_SYS}"

EXTRA_OECONF = "${@['--enable-clocale=generic', ''][bb.data.getVar('USE_NLS', d, 1) != 'no']} \
                --with-gnu-ld \
                --enable-shared \
                --enable-multilib \
                --enable-target-optspace \
                --enable-languages=c,c++,f77 \
                --enable-threads=posix \
                --enable-c99 \
                --enable-long-long \
                --enable-symvers=gnu \
                --program-prefix=${TARGET_PREFIX} \
                ${EXTRA_OECONF_PATHS} \
                ${EXTRA_OECONF_DEP}"

EXTRA_OECONF_PATHS = "--with-local-prefix=${prefix}/local \
                      --with-gxx-include-dir=${includedir}/c++/${PV}"

EXTRA_OECONF_DEP = ""
EXTRA_OECONF_uclibc = "--disable-__cxa_atexit"
EXTRA_OECONF_glibc = "--enable-__cxa_atexit"
EXTRA_OECONF += "${@get_gcc_fpu_setting(bb, d)}"

def get_gcc_fpu_setting(bb, d):
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		return "--with-float=soft"
	return ""

python __anonymous () {
    import bb, re
    if (re.match('linux-uclibc$', bb.data.getVar('TARGET_OS', d, 1)) != None):
        bb.data.setVar('EXTRA_OECONF_DEP', '${EXTRA_OECONF_uclibc}', d)
    elif (re.match('linux$', bb.data.getVar('TARGET_OS', d, 1)) != None):
        bb.data.setVar('EXTRA_OECONF_DEP', '${EXTRA_OECONF_glibc}', d)
}

do_configure () {
	# Setup these vars for cross building only
	if [ "${BUILD_SYS}" != "${HOST_SYS}" ]; then
		export CC_FOR_TARGET="${CCACHE} ${HOST_PREFIX}gcc"
		export GCC_FOR_TARGET="${CCACHE} ${HOST_PREFIX}gcc"
		export CXX_FOR_TARGET="${CCACHE} ${HOST_PREFIX}g++"
		export AS_FOR_TARGET="${HOST_PREFIX}as"
		export LD_FOR_TARGET="${HOST_PREFIX}ld"
		export NM_FOR_TARGET="${HOST_PREFIX}nm"
		export AR_FOR_TARGET="${HOST_PREFIX}ar"
		export RANLIB_FOR_TARGET="${HOST_PREFIX}ranlib"
	fi
	(cd ${S} && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_install () {
	autotools_do_install

	# Cleanup some of the gcc-lib stuff
	rm -rf ${D}${libdir}/gcc-lib/${TARGET_SYS}/${PV}/install-tools

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	mv -f ${D}${libdir}/libgcc_s.so.* ${D}${base_libdir}
	rm -f ${D}${libdir}/libgcc_s.so
	ln -sf `echo ${libdir}/gcc-lib/${TARGET_SYS}/${PV} | tr -s / |
		sed -e 's,^/,,' -e 's,[^/]*,..,g'`/lib/libgcc_s.so.? \
		${D}${libdir}/gcc-lib/${TARGET_SYS}/${PV}/libgcc_s.so

	# Cleanup manpages..
	rm -rf ${D}${mandir}/man7
	mv ${D}${mandir}/man1/cpp.1 \
	   ${D}${mandir}/man1/${TARGET_SYS}-cpp.1
	mv ${D}${mandir}/man1/gcov.1 \
	   ${D}${mandir}/man1/${TARGET_SYS}-gcov.1

	# We use libiberty from binutils
	rm -f ${D}${libdir}/libiberty.a

	cd ${D}${bindir}

	# We care about g++ not c++
	rm -f *c++

	# We don't care about the gcc-<version> ones for this
	rm -f *gcc-?.?*

	# These sometimes show up, they are strange, we remove them
	rm -f ${TARGET_ARCH}-*${TARGET_ARCH}-*

	# Symlinks so we can use these trivially on the target
	ln -sf ${TARGET_SYS}-g77 g77
	ln -sf ${TARGET_SYS}-g++ g++
	ln -sf ${TARGET_SYS}-gcc gcc
	ln -sf g77 f77
	ln -sf g++ c++
	ln -sf gcc cc
}
