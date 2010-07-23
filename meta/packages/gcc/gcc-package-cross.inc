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

	# gcc-runtime installs libgcc into a special location in staging since it breaks doing a standalone build
	if [ "${PN}" == "gcc-cross" -o "${PN}" == "gcc-crosssdk" ]; then
		dest=${D}/${includedir}/gcc-build-internal-${MULTIMACH_TARGET_SYS}
		oe_runmake "DESTDIR=$dest" libdir=${target_libdir} base_libdir=${target_base_libdir} prefix=${target_prefix} exec_prefix=${target_exec_prefix} install-target-libgcc

		# Ideally here we'd override the libgcc Makefile's idea of slibdir but
		# for now, we just move the files to the correct location

		install -d $dest${target_base_libdir}
		mv $dest${target_exec_prefix}/${TARGET_SYS}/lib*/* $dest${target_base_libdir}
		rm -rf $dest${target_exec_prefix}/${TARGET_SYS}

		# Also need to move gcc from /usr/lib/gcc/* to /usr/lib/ else the search paths won't find the crt*.o files

		mv $dest${target_libdir}/gcc/* $dest${target_libdir}/
		rmdir $dest${target_libdir}/gcc
	fi
}
