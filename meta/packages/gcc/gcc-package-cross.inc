INHIBIT_PACKAGE_STRIP ?= ""
OLD_INHIBIT_PACKAGE_STRIP := "${INHIBIT_PACKAGE_STRIP}"
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "libgcc libssp libstdc++ libg2c libgfortran"

# Called from within gcc-cross, so libdir is set wrong
FILES_libg2c = "${target_libdir}/libg2c.so.*"

FILES_libssp = "${target_libdir}/libssp.so.*"

FILES_libgfortran = "${target_libdir}/libgfortran.so.*"

FILES_libgcc = "${target_base_libdir}/libgcc_s.so.1"

FILES_libstdc++ = "${target_libdir}/libstdc++.so.*"

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	install -d ${D}${target_base_libdir}
	install -d ${D}${target_libdir}

	for d in ${TARGET_SYS}/lib/nof ${TARGET_SYS}/lib64 ${TARGET_SYS}/lib; do
		if [ -d ${D}${prefix}/$d/ ]; then
			mv -f ${D}${prefix}/$d/libgcc* ${D}${target_base_libdir} || true
			mv -f ${D}${prefix}/$d/* ${D}${target_libdir} || true
		fi
	done

	# Manually run the target stripper since we won't get it run by
	# the packaging.
	if [ "x${OLD_INHIBIT_PACKAGE_STRIP}" != "x1" ]; then
		${TARGET_PREFIX}strip ${D}${target_libdir}/libstdc++.so.* || true
		${TARGET_PREFIX}strip ${D}${target_libdir}/libg2c.so.* || true
		${TARGET_PREFIX}strip ${D}${target_base_libdir}/libgcc_s.so.* || true
		${TARGET_PREFIX}strip ${D}${target_libdir}/libgfortran*.so* || true
	fi
     
	# Link gfortran to g77 to satisfy not-so-smart configure or hard coded g77
	# gfortran is fully backwards compatible. This is a safe and practical solution. 
	ln -sf ${CROSS_DIR}/bin/${TARGET_PREFIX}gfortran ${CROSS_DIR}/bin/${TARGET_PREFIX}g77 || true
	ln -sf ${CROSS_DIR}/${TARGET_SYS}/bin/gfortran ${CROSS_DIR}/${TARGET_SYS}/bin/g77 || true

	# Remove things we don't need but keep share/java
	for d in info man share/doc share/locale share/man share/info; do
		rm -rf ${D}${CROSS_DIR}/$d
	done

	#fix up libsupc++ and libstdc++ la files
	for f in libssp.la libssp_nonshared.la libsupc++.la libstdc++.la ; do
		# Remove any -L options from dependency_libs - we should never need them as the 
		# files are in the sysroot
		sed -i "s|\(dependency_libs\s*=.*\)-L\s*[^ ]*\(.*\)|\1\2|g" ${D}${target_libdir}/$f || true
		# Set libdir correctly 
		sed -i "s|libdir\s*=.*|libdir='${target_libdir}'|g" ${D}${target_libdir}/$f || true
	done
}
