require gcc-${PV}.inc

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

PACKAGES = "\
  ${PN} \
  ${PN}-dev \
  "

FILES_${PN} = "${base_libdir}/libgcc*.so.*"
FILES_${PN}-dev = " \
  ${base_libdir}/libgcc*.so \
  ${libdir}/${TARGET_SYS}/${BINV}/crt* \
  ${libdir}/${TARGET_SYS}/${BINV}/libgcov.a \
  ${libdir}/${TARGET_SYS}/${BINV}/libgcc*"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	target=`echo ${MULTIMACH_TARGET_SYS} | sed -e s#-nativesdk##`

	# Install libgcc from our gcc-cross saved data
	install -d ${D}${base_libdir} ${D}${libdir}
	cp -fpPR ${STAGING_INCDIR_NATIVE}/gcc-build-internal-$target/* ${D}

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	if [ -f ${D}${libdir}/nof/libgcc_s.so ]; then
		mv ${D}${libdir}/nof/libgcc* ${D}${base_libdir}
	else
		mv ${D}${libdir}/libgcc* ${D}${base_libdir} || true
	fi

	chown -R root:root ${D}
	chmod +x ${D}${base_libdir}/libgcc_s.so.*
}

do_package_write_ipk[depends] += "virtual/${MLPREFIX}libc:do_package"
do_package_write_deb[depends] += "virtual/${MLPREFIX}libc:do_package"
do_package_write_rpm[depends] += "virtual/${MLPREFIX}libc:do_package"

BBCLASSEXTEND = "nativesdk"

