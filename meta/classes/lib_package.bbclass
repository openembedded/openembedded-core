PACKAGES =+ "${PN}-bin"

FILES_${PN} = "${libexecdir} ${libdir}/lib*${SOLIBS} \
	    ${sysconfdir} ${sharedstatedir} ${localstatedir} \
	    ${base_libdir}/*${SOLIBS} \
	    ${datadir}/${BPN} ${libdir}/${BPN}"

FILES_${PN}-bin = "${bindir}/* ${sbindir}/* /bin/* /sbin/*"
