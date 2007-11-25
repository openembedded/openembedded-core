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

	# Manually run the target stripper since we won't get it run by
	# the packaging.
	if [ "x${OLD_INHIBIT_PACKAGE_STRIP}" != "x1" ]; then
		${TARGET_PREFIX}strip ${D}${prefix}/${TARGET_SYS}/lib/libstdc++.so.*
		${TARGET_PREFIX}strip ${D}${prefix}/${TARGET_SYS}/lib/libg2c.so.* || true
		${TARGET_PREFIX}strip ${D}${prefix}/${TARGET_SYS}/lib/libgcc_s.so.*
	fi
}

FILES_libgcc = "${prefix}/${TARGET_SYS}/lib/libgcc*.so.*"
FILES_libgcc-dev = "${prefix}/${TARGET_SYS}/lib/libgcc*.so"

FILES_libstdc++ = "${prefix}/${TARGET_SYS}/lib/libstdc++.so.*"
FILES_libstdc++-dev = "${includedir}/c++/${BINV} \
                       ${prefix}/${TARGET_SYS}/lib/libstdc++.so \
                       ${prefix}/${TARGET_SYS}/lib/libstdc++.a \
                       ${prefix}/${TARGET_SYS}/lib/libsupc++.a"

FILES_libmudflap = "${prefix}/${TARGET_SYS}/lib/libmudflap*.so.*"
FILES_libmudflap-dev = "${prefix}/${TARGET_SYS}/lib/libmudflap*.so \
                        ${prefix}/${TARGET_SYS}/lib/libmudflap*.a"

RRECOMMENDS_${PN} += "libgcc libgcc-dev cpp g++ libstdc++ libstdc++-dev"
