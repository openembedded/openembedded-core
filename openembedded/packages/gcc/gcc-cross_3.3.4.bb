SECTION = "devel"
include gcc_${PV}.bb
inherit cross
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

# Files for these are defined in the main gcc.oe
PACKAGES = "libgcc libstdc++ libg2c"
OLD_INHIBIT_PACKAGE_STRIP := "${INHIBIT_PACKAGE_STRIP}"
INHIBIT_PACKAGE_STRIP = "1"

EXTRA_OECONF_PATHS = "--with-local-prefix=${CROSS_DIR}/${TARGET_SYS} \
		--with-gxx-include-dir=${CROSS_DIR}/${TARGET_SYS}/include/c++"

export CPPFLAGS = ""
export CXXFLAGS = ""
export CFLAGS = ""
export LDFLAGS = ""

do_configure () {
	export CC="${BUILD_CC}"
	export AR="${TARGET_SYS}-ar"
	export RANLIB="${TARGET_SYS}-ranlib"
	export LD="${TARGET_SYS}-ld"
	export NM="${TARGET_SYS}-nm"
	rm -f ${CROSS_DIR}/lib/gcc-lib/${TARGET_SYS}/${PV}/libgcc_eh.a
	(cd ${S} && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_compile_prepend () {
	export CC="${BUILD_CC}"
	export AR_FOR_TARGET="${TARGET_SYS}-ar"
	export RANLIB_FOR_TARGET="${TARGET_SYS}-ranlib"
	export LD_FOR_TARGET="${TARGET_SYS}-ld"
	export NM_FOR_TARGET="${TARGET_SYS}-nm"
	export CC_FOR_TARGET="${CCACHE} ${TARGET_SYS}-gcc"
}

do_stage_append () {
	for d in info man share/doc share/locale ; do
		rm -rf ${CROSS_DIR}/$d
	done

	# These aren't useful on the cross toolchain
	rm -f ${CROSS_DIR}/bin/*gcov
	rm -f ${CROSS_DIR}/bin/*gccbug

	# Fix a few include links so cross builds are happier
	if [ ! -e ${STAGING_INCDIR}/c++ ]; then
		mkdir -p ${STAGING_INCDIR}
		rm -f ${STAGING_INCDIR}/c++
		ln -sf ${CROSS_DIR}/${TARGET_SYS}/include/c++ \
			${STAGING_INCDIR}/
	fi

	# We use libiberty from binutils
	rm -f ${CROSS_DIR}/lib/libiberty.a

	# We probably don't need these
	rmdir ${CROSS_DIR}/include || :

	# We don't really need to keep this around
	rm -rf ${CROSS_DIR}/share
}

python do_package() {
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_libgcc', 'libgcc1', d)
	bb.build.exec_func('package_do_package', d)
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	if [ "${BUILD_SYS}" == "${TARGET_SYS}" ]; then
		# native builds drop one pathname component
		mv -f ${D}${prefix}/lib/libgcc_s.so.* ${D}${base_libdir}
	else
		mv -f ${D}${prefix}/*/lib/libgcc_s.so.* ${D}${base_libdir}
	fi

	# Move libstdc++ and libg2c into libdir (resetting our prefix to /usr
	TGT_LIBDIR=`echo ${libdir} | sed -e 's,${CROSS_DIR},/usr,'`
	mkdir -p ${D}${TGT_LIBDIR}
	mv -f ${D}${prefix}/*/lib/libstdc++.so.* ${D}${TGT_LIBDIR}
	mv -f ${D}${prefix}/*/lib/libg2c.so.* ${D}${TGT_LIBDIR}

	# Manually run the target stripper since we won't get it run by
	# the packaging.
	if [ "x${OLD_INHIBIT_PACKAGE_STRIP}" != "x1" ]; then
		${TARGET_PREFIX}strip ${D}${TGT_LIBDIR}/libstdc++.so.*
		${TARGET_PREFIX}strip ${D}${TGT_LIBDIR}/libg2c.so.*
		${TARGET_PREFIX}strip ${D}${base_libdir}/libgcc_s.so.*
	fi
}
