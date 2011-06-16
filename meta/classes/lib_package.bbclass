PACKAGES += "${PN}-bin"

FILES_${PN} = "${libexecdir} ${libdir}/lib*${SOLIBS} \
	    ${sysconfdir} ${sharedstatedir} ${localstatedir} \
	    ${base_libdir}/*${SOLIBS} \
	    ${datadir}/${PN} ${libdir}/${PN}"
FILES_${PN}-dev = "${includedir} ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la \
		${libdir}/*.o ${libdir}/pkgconfig /lib/*.o \
		${datadir}/aclocal ${bindir}/*-config"
FILES_${PN}-bin = "${bindir}/* ${sbindir}/* /bin/* /sbin/*"
