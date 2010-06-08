INHIBIT_PACKAGE_STRIP = "1"

do_install () {
	oe_runmake 'DESTDIR=${D}' install-host

	install -d ${D}${target_base_libdir}
	install -d ${D}${target_libdir}
    
	# Link gfortran to g77 to satisfy not-so-smart configure or hard coded g77
	# gfortran is fully backwards compatible. This is a safe and practical solution. 
	ln -sf ${STAGING_DIR_NATIVE}${prefix_native}/bin/${TARGET_PREFIX}gfortran ${STAGING_DIR_NATIVE}${prefix_native}/bin/${TARGET_PREFIX}g77 || true
	ln -sf ${STAGING_DIR_NATIVE}${prefix_native}/${TARGET_SYS}/bin/gfortran ${STAGING_DIR_NATIVE}/${prefix_native}/${TARGET_SYS}/bin/g77 || true

	# Remove things we don't need but keep share/java
	for d in info man share/doc share/locale share/man share/info; do
		rm -rf ${D}${STAGING_DIR_NATIVE}${prefix_native}/$d
	done

	# gcc-runtime requires some headers, we stash them here
	if [ "${PN}" == "gcc-cross" -o "${PN}" == "gcc-crosssdk" ]; then
		install -d ${D}${includedir}/gcc-build-internal-${TARGET_SYS}
		for f in *.h *.c libgcc.mvars; do
			cp ${B}/gcc/$f ${D}${includedir}/gcc-build-internal-${TARGET_SYS}/
		done
		# Special for PowerPC (and possibly others)
		if [ -f ${B}/gcc/tramp.S ]; then
			cp ${B}/gcc/*.S ${D}${includedir}/gcc-build-internal-${TARGET_SYS}/
		fi
		if [ -f libgcc.mvars]; then
			cp ${B}/gcc/libgcc.mvars ${D}${includedir}/gcc-build-internal-${TARGET_SYS}/
		fi
	fi
}
