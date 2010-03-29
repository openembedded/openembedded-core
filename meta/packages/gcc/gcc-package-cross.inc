INHIBIT_PACKAGE_STRIP = "1"

do_install () {
	oe_runmake 'DESTDIR=${D}' install-host

	install -d ${D}${target_base_libdir}
	install -d ${D}${target_libdir}
    
	# Link gfortran to g77 to satisfy not-so-smart configure or hard coded g77
	# gfortran is fully backwards compatible. This is a safe and practical solution. 
	ln -sf ${CROSS_DIR}/bin/${TARGET_PREFIX}gfortran ${CROSS_DIR}/bin/${TARGET_PREFIX}g77 || true
	ln -sf ${CROSS_DIR}/${TARGET_SYS}/bin/gfortran ${CROSS_DIR}/${TARGET_SYS}/bin/g77 || true

	# Remove things we don't need but keep share/java
	for d in info man share/doc share/locale share/man share/info; do
		rm -rf ${D}${CROSS_DIR}/$d
	done

	# gcc-runtime requires some headers, we stash them here
	install -d ${D}${includedir}/gcc-build-internal
	for f in auto-host.h gcov-iov.h gthr-default.h libgcc.mvars options.h tconfig.h tm.h; do
		cp ${B}/gcc/$f ${D}${includedir}/gcc-build-internal/
	done
}
