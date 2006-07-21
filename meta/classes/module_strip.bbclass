#DEPENDS_append = " module-strip"

do_strip_modules () {
	for p in ${PACKAGES}; do
		if test -e ${WORKDIR}/install/$p/lib/modules; then
			modules="`find ${WORKDIR}/install/$p/lib/modules -name \*${KERNEL_OBJECT_SUFFIX}`"
			if [ -n "$modules" ]; then
				${STRIP} -v -g $modules
#				NM="${CROSS_DIR}/bin/${HOST_PREFIX}nm" OBJCOPY="${CROSS_DIR}/bin/${HOST_PREFIX}objcopy" strip_module $modules
			fi
		fi
	done
}

python do_package_append () {
	if (bb.data.getVar('INHIBIT_PACKAGE_STRIP', d, 1) != '1'):
		bb.build.exec_func('do_strip_modules', d)
}
