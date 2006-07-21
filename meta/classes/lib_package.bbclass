PACKAGES = "${PN} ${PN}-dev ${PN}-doc ${PN}-bin"

FILES_${PN} = "${libexecdir} ${libdir}/lib*.so.* \
	    ${sysconfdir} ${sharedstatedir} ${localstatedir} \
	    /lib/*.so* ${datadir}/${PN} ${libdir}/${PN}"
FILES_${PN}-dev = "${includedir} ${libdir}/lib*.so ${libdir}/*.la \
		${libdir}/*.a ${libdir}/pkgconfig /lib/*.a /lib/*.o \
		${datadir}/aclocal ${bindir}/*-config"
FILES_${PN}-bin = "${bindir} ${sbindir} /bin /sbin"
