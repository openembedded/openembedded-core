DESCRIPTION = "libjpeg is a library for handling the JPEG (JFIF) image format."
LICENSE ="jpeg"
SECTION = "libs"
PRIORITY = "required"

DEPENDS = "libtool-cross"

PR = "r5"

SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian.patch;patch=1 \
	   file://ldflags.patch;patch=1 \
	   file://paths.patch;patch=1"
S = "${WORKDIR}/jpeg-${PV}"

inherit autotools 

EXTRA_OECONF="--enable-static --enable-shared"
EXTRA_OEMAKE='"LIBTOOL=${STAGING_BINDIR_NATIVE}/${HOST_SYS}-libtool"'

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

PACKAGES =+ 		"jpeg-tools "
FILES_jpeg-tools = 	"${bindir}/*"


