ZLIB_EXTRA = ""
inherit sdk

do_stage() {
        install -d ${STAGING_INCDIR}
        install -m 0644 zlib.h ${STAGING_INCDIR}/
        install -m 0644 zconf.h ${STAGING_INCDIR}/
        install -m 0644 zlibdefs.h ${STAGING_INCDIR}/
        oe_libinstall -a -so libz ${STAGING_LIBDIR}
}
