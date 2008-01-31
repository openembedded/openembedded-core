inherit sdk
DEPENDS += "flex-native bison-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/binutils-${PV}"
EXTRA_OECONF = "--with-sysroot=${prefix}/${TARGET_SYS} \
		--program-prefix=${TARGET_PREFIX}"

do_stage() {
	:
}

do_install () {
        autotools_do_install

	# Fix the ${prefix}/${TARGET_SYS}/bin/* files to be symlinks
	rm ${D}${prefix}/${TARGET_SYS}/bin/.debug -Rf
	rm ${D}${prefix}/${TARGET_SYS}/bin/*
	for l in ${D}${bindir}/*; do
		ln -sf "${bindir}/`basename $l`" "${D}${prefix}/${TARGET_SYS}/bin/`basename $l | sed -e 's,${TARGET_PREFIX},,'`"
	done

        # Install the libiberty header
        install -d ${D}${includedir}
        install -m 644 ${S}/include/ansidecl.h ${D}${includedir}
        install -m 644 ${S}/include/libiberty.h ${D}${includedir}
}
