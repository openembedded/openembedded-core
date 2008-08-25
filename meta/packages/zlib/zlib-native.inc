ZLIB_EXTRA = ""
inherit native

do_stage() {
        install -m 0644 zlib.h ${STAGING_INCDIR}/
        install -m 0644 zconf.h ${STAGING_INCDIR}/
        install -m 0644 zlibdefs.h ${STAGING_INCDIR}/
        oe_libinstall -a -so libz ${STAGING_LIBDIR}
}

DEPENDS = "libtool-native"
