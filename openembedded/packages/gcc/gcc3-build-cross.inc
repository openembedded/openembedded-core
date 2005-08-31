USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibc", "no", "", d )}'

EXTRA_OECONF_PATHS = "--with-local-prefix=${CROSS_DIR}/${TARGET_SYS} \
		      --with-gxx-include-dir=${CROSS_DIR}/${TARGET_SYS}/include/c++"

do_configure_prepend () {
	rm -f ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/libgcc_eh.a
}

do_compile_prepend () {
	export CC="${BUILD_CC}"
	export AR_FOR_TARGET="${TARGET_SYS}-ar"
	export RANLIB_FOR_TARGET="${TARGET_SYS}-ranlib"
	export LD_FOR_TARGET="${TARGET_SYS}-ld"
	export NM_FOR_TARGET="${TARGET_SYS}-nm"
	export CC_FOR_TARGET="${CCACHE} ${TARGET_SYS}-gcc ${TARGET_CC_ARCH}"
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
