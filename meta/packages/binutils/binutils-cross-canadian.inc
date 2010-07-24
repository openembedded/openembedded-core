inherit cross-canadian
DEPENDS = "flex-native bison-native virtual/${HOST_PREFIX}gcc-crosssdk virtual/libc-nativesdk zlib-nativesdk gettext-nativesdk"
EXTRA_OECONF = "--with-sysroot=${SDKPATH}/sysroots/${TARGET_SYS} \
		--program-prefix=${TARGET_PREFIX}"

do_install () {
        autotools_do_install

	# Fix the ${prefix}/${TARGET_SYS}/bin/* files to be symlinks
	rm ${D}${prefix}/${TARGET_SYS}/bin/.debug -Rf
	rm ${D}${prefix}/${TARGET_SYS}/bin/*
	for l in ${D}${bindir}/*; do
		ln -sf "${bindir}/`basename $l`" "${D}${prefix}/${TARGET_SYS}/bin/`basename $l | sed -e 's,${TARGET_PREFIX},,'`"
	done

	# We're not interested in the libs or headers, these would come from the 
	# nativesdk or target version of the binutils recipe
	rm -f ${D}${libdir}/libbfd*
	rm -f ${D}${libdir}/libiberty*
	rm -f ${D}${libdir}/libopcodes*
	rm -f ${D}${includedir}/*.h
}
