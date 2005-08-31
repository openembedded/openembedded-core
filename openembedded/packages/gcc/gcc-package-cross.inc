# Packages emitted by our gcc-cross builds.
#
INHIBIT_PACKAGE_STRIP ?= ""
HAS_G2C ?= "yes"
HAS_GFORTRAN ?= "no"
OLD_INHIBIT_PACKAGE_STRIP := "${INHIBIT_PACKAGE_STRIP}"
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "libgcc libstdc++ libg2c libg2c-dev libgfortran libgfortran-dev"

PACKAGE_ARCH_libg2c = "${TARGET_ARCH}"
PACKAGE_ARCH_libg2c-dev = "${TARGET_ARCH}"
PACKAGE_ARCH_libgfortran = "${TARGET_ARCH}"
PACKAGE_ARCH_libgfortran-dev = "${TARGET_ARCH}"

# Called from within gcc-cross, so libdir is set wrong
FILES_libg2c = "${target_libdir}/libg2c.so.*"
FILES_libg2c-dev = "${target_libdir}/libg2c.so \
		    ${target_libdir}/libg2c.a \
		    ${target_libdir}/libfrtbegin.a"

FILES_libgfortran = "${target_libdir}/libgfortran.so.*"
FILES_libgfortran-dev = "${target_libdir}/libgfortran.a \
			 ${target_libdir}/libgfortran.so \
			 ${target_libdir}/libgfortranbegin.a"

PACKAGE_ARCH_libgcc = "${TARGET_ARCH}"
FILES_libgcc = "${target_base_libdir}/libgcc_s.so.1"

PACKAGE_ARCH_libstdc++ = "${TARGET_ARCH}"
PACKAGE_ARCH_libstdc++-dev = "${TARGET_ARCH}"
FILES_libstdc++ = "${target_libdir}/libstdc++.so.*"
FILES_libstdc++-dev = "${target_includedir}/c++/${PV} \
		       ${target_libdir}/libstdc++.so \
		       ${target_libdir}/libstdc++.la \
		       ${target_libdir}/libstdc++.a \
		       ${target_libdir}/libsupc++.la \
		       ${target_libdir}/libsupc++.a"

python do_package() {
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_libgcc', 'libgcc1', d)
	bb.build.exec_func('package_do_package', d)
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# Move libgcc_s into /lib
	mkdir -p ${D}${target_base_libdir}
	if [ -f ${D}${target_base_libdir}/libgcc_s.so.? ]; then
		# Already in the right location
		:
	elif [ -f ${D}${prefix}/lib/libgcc_s.so.? ]; then
		mv -f ${D}${prefix}/lib/libgcc_s.so.* ${D}${target_base_libdir}
	else
		mv -f ${D}${prefix}/*/lib/libgcc_s.so.* ${D}${target_base_libdir}
	fi

	# Move libstdc++ and libg2c into libdir (resetting our prefix to /usr
	mkdir -p ${D}${target_libdir}
	mv -f ${D}${prefix}/*/lib/libstdc++.so.* ${D}${target_libdir}
	if [ "${HAS_G2C}" = "yes" ]; then
	  mv -f ${D}${prefix}/*/lib/libg2c.so.* ${D}${target_libdir}
	fi
	if [ "${HAS_GFORTRAN}" = "yes" ]; then
	  mv -f ${D}${prefix}/*/lib/libgfortran*.so.* ${D}${target_libdir}
	fi

       # Manually run the target stripper since we won't get it run by
       # the packaging.
       if [ "x${OLD_INHIBIT_PACKAGE_STRIP}" != "x1" ]; then
               ${TARGET_PREFIX}strip ${D}${target_libdir}/libstdc++.so.*
               ${TARGET_PREFIX}strip ${D}${target_libdir}/libg2c.so.*
               ${TARGET_PREFIX}strip ${D}${base_libdir}/libgcc_s.so.*
       fi
}
