# Packages emitted by our gcc-cross builds.
#
INHIBIT_PACKAGE_STRIP ?= ""
HAS_G2C ?= "yes"
HAS_GFORTRAN ?= "no"
OLD_INHIBIT_PACKAGE_STRIP := "${INHIBIT_PACKAGE_STRIP}"
INHIBIT_PACKAGE_STRIP = "1"


python do_package() {
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_libgcc', 'libgcc1', d)
	bb.build.exec_func('package_do_package', d)
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	if [ -f ${D}${base_libdir}/libgcc_s.so.? ]; then
		# Already in the right location
		:
	elif [ -f ${D}${prefix}/lib/libgcc_s.so.? ]; then
		mv -f ${D}${prefix}/lib/libgcc_s.so.* ${D}${base_libdir}
	else
		mv -f ${D}${prefix}/*/lib/libgcc_s.so.* ${D}${base_libdir}
	fi

	# Move libstdc++ and libg2c into libdir (resetting our prefix to /usr
	mkdir -p ${D}${libdir}
	mv -f ${D}${prefix}/*/lib/libstdc++.so.* ${D}${libdir}
	if [ "${HAS_G2C}" = "yes" ]; then
	  mv -f ${D}${prefix}/*/lib/libg2c.so.* ${D}${libdir}
	fi
	if [ "${HAS_GFORTRAN}" = "yes" ]; then
	  mv -f ${D}${prefix}/*/lib/libgfortran*.so.* ${D}${libdir}
	fi

       # Manually run the target stripper since we won't get it run by
       # the packaging.
       if [ "x${OLD_INHIBIT_PACKAGE_STRIP}" != "x1" ]; then
               ${TARGET_PREFIX}strip ${D}${libdir}/libstdc++.so.*
               ${TARGET_PREFIX}strip ${D}${libdir}/libg2c.so.* || true
               ${TARGET_PREFIX}strip ${D}${base_libdir}/libgcc_s.so.*
       fi
}
