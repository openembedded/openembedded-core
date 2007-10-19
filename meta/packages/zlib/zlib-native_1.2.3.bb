SECTION = "libs"
require zlib_${PV}.bb
ZLIB_EXTRA = ""
inherit native

do_stage() {
        install -m 0644 zlib.h ${STAGING_INCDIR}/zlib.h
        install -m 0644 zconf.h ${STAGING_INCDIR}/zconf.h
        oe_libinstall -a -so libz ${STAGING_LIBDIR}
}


DEPENDS = "libtool-native"
