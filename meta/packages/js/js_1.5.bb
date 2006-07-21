LICENSE = "GPL"
DESCRIPTION = "A JavaScript engine"
SECTION = "libs"
DEPENDS = "readline"
SRC_URI = "http://ftp.mozilla.org/pub/mozilla.org/js/js-1.5.tar.gz \
	   file://jsautocfg.h"
MAINTAINER = "Chris Lord <chris@openedhand.com>"

S = "${WORKDIR}/js/src"

FILES_${PN} = "${libdir}/lib*.so"
FILES_${PN}-dev = "${includedir} ${libdir}/lib*.a"

EXTRA_OEMAKE = "'CC=${CC}' 'LD=${LD}' 'XCFLAGS=${CFLAGS}' 'XLDFLAGS=-L${STAGING_LIBDIR} -soname=libjs'"

do_compile_prepend() {
	cp ${WORKDIR}/jsautocfg.h ${S}/
}

do_compile() {
	oe_runmake -f Makefile.ref JS_READLINE=1 JS_EDITLINE=1 PREBUILT_CPUCFG=1
}

do_install() {
	install -d ${D}${libdir}
	install -d ${D}${includedir}
	install -d ${D}${includedir}/js
	oe_libinstall -so -C Linux_All_DBG.OBJ libjs ${D}${libdir}
	install -m 0644 ${S}/*.h ${D}${includedir}/js
}

do_stage() {
	install -d ${STAGING_INCDIR}/js
	install -m 0644 ${S}/*.h ${STAGING_INCDIR}/js/
	oe_libinstall -so -C Linux_All_DBG.OBJ libjs ${STAGING_LIBDIR}
}
