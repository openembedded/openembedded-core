SECTION = "libs"
include jpeg_${PV}.bb
inherit native
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/jpeg-${PV}"
DEPENDS = ""

do_stage() {
        install -m 644 jconfig.h ${STAGING_INCDIR}/jconfig.h
        install -m 644 jpeglib.h ${STAGING_INCDIR}/jpeglib.h
        install -m 644 jmorecfg.h ${STAGING_INCDIR}/jmorecfg.h
        install -m 644 jerror.h ${STAGING_INCDIR}/jerror.h
        install -m 644 jpegint.h ${STAGING_INCDIR}/jpegint.h
        oe_libinstall -so libjpeg ${STAGING_LIBDIR}
}
