require libpng_${PV}.bb
inherit native
DEPENDS = "zlib-native"

INHIBIT_NATIVE_STAGE_INSTALL = "1"

do_stage_append() {
        cp libpng.pc libpng12.pc
        install -m 644 png.h ${STAGING_INCDIR}/png.h
        install -m 644 pngconf.h ${STAGING_INCDIR}/pngconf.h
        oe_libinstall -so libpng12 ${STAGING_LIBDIR}/
        ln -sf libpng12.so ${STAGING_LIBDIR}/libpng.so
}
