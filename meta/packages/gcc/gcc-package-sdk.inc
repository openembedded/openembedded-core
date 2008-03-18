INHIBIT_PACKAGE_STRIP ?= ""
OLD_INHIBIT_PACKAGE_STRIP := "${INHIBIT_PACKAGE_STRIP}"
INHIBIT_PACKAGE_STRIP = "1"

# Having anything auto depending on gcc-cross-sdk is a really bad idea...
EXCLUDE_FROM_SHLIBS = "1"

PACKAGES = "${PN} ${PN}-doc"

FILES_${PN} = "\
    ${bindir}/* \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1 \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/collect2 \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/f771 \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1plus \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/*.o \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/specs \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/lib* \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/include \
    ${includedir}/c++/${BINV} \
    ${prefix}/${TARGET_SYS}/bin/* \
    ${prefix}/${TARGET_SYS}/lib/* \
    "
FILES_${PN}-doc = "\
    ${infodir} \
    ${mandir} \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/include/README \
    "

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# Cleanup some of the ${libdir}{,exec}/gcc stuff ...
	rm -r ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -r ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/install-tools

	# We care about g++ not c++
	rm -f ${D}${bindir}/*c++

	# We don't care about the gcc-<version> copies
	rm -f ${D}${bindir}/*gcc-?.?*

	# We use libiberty from binutils
	rm -f ${D}${prefix}/${TARGET_SYS}/lib/libiberty.a
	rm -f ${D}${libdir}/libiberty.a

	# Create the ${prefix}/${TARGET_SYS}/bin/* symlinks
	install -d ${D}${prefix}/${TARGET_SYS}/bin/
	for l in ${D}${bindir}/*; do
		ln -sf "${bindir}/`basename $l`" "${D}${prefix}/${TARGET_SYS}/bin/`basename $l | sed -e 's,${TARGET_PREFIX},,'`"
	done
	ln -sf "${bindir}/${TARGET_PREFIX}gcc" "${D}${prefix}/${TARGET_SYS}/bin/cc"

	# Manually run the target stripper since we won't get it run by
	# the packaging.
	if [ "x${OLD_INHIBIT_PACKAGE_STRIP}" != "x1" ]; then
		${TARGET_PREFIX}strip ${D}${prefix}/${TARGET_SYS}/lib/libstdc++.so.*
		${TARGET_PREFIX}strip ${D}${prefix}/${TARGET_SYS}/lib/libg2c.so.* || true
		${TARGET_PREFIX}strip ${D}${prefix}/${TARGET_SYS}/lib/libgcc_s.so.*
	fi
}

