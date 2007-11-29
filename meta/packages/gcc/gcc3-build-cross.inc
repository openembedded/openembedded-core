USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibc", "no", "", d )}'

EXTRA_OECONF_PATHS = "--with-local-prefix=${STAGING_DIR_TARGET}${layout_prefix} \
		      --with-gxx-include-dir=${STAGING_DIR_TARGET}/${layout_includedir}/c++ \
                      --with-sysroot=${STAGING_DIR_TARGET} \
                      --with-build-sysroot=${STAGING_DIR_TARGET}"

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

	# We use libiberty from binutils
	rm -f ${CROSS_DIR}/lib/libiberty.a

	# We probably don't need these
	rmdir ${CROSS_DIR}/include || :

	# We don't really need to keep this around
	# rm -rf ${CROSS_DIR}/share but leave java there
	for d in share/man share/info;
	do
		rm -rf ${CROSS_DIR}/$d
	done

	#fix up libsupc++ and libstdc++ la files
	sed -i "s|dependency_libs\s*=\s*.*|dependency_libs='-L${CROSS_DIR}/${TARGET_SYS}/lib -lgcc_s -lc -lm '|" ${CROSS_DIR}/${TARGET_SYS}/lib/libsupc++.la || true
	sed -i "s|dependency_libs\s*=\s*.*|dependency_libs='-L${CROSS_DIR}/${TARGET_SYS}/lib -lgcc_s -lc -lm '|" ${CROSS_DIR}/${TARGET_SYS}/lib/libstdc++.la || true
}

do_package_write_ipk[depends] += "virtual/libc:do_package"
