SECTION = "libs"
PRIORITY = "required"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
DEPENDS = "libtool-cross"
DESCRIPTION = "libjpeg is a library for handling the JPEG (JFIF) image format."
PACKAGES =+ "jpeg-tools "
FILES_jpeg-tools = "${bindir}"
LICENSE ="jpeg"
SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian.patch;patch=1 \
	   file://ldflags.patch;patch=1 \
	   file://paths.patch;patch=1"
S = "${WORKDIR}/jpeg-${PV}"

inherit autotools 

EXTRA_OECONF="--enable-static --enable-shared"
EXTRA_OEMAKE='"LIBTOOL=${STAGING_BINDIR}/${HOST_SYS}-libtool"'

CFLAGS_append = " -D_REENTRANT"

do_stage() {
	install -m 644 jconfig.h ${STAGING_INCDIR}/jconfig.h
	install -m 644 jpeglib.h ${STAGING_INCDIR}/jpeglib.h
	install -m 644 jmorecfg.h ${STAGING_INCDIR}/jmorecfg.h
	install -m 644 jerror.h ${STAGING_INCDIR}/jerror.h
	install -m 644 jpegint.h ${STAGING_INCDIR}/jpegint.h
	oe_libinstall -so libjpeg ${STAGING_LIBDIR}
}

do_install() {
	install -d ${D}${bindir} ${D}${includedir} \
		   ${D}${mandir}/man1 ${D}${libdir}
	oe_runmake 'DESTDIR=${D}' install
}
