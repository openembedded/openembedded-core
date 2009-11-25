DESCRIPTION = "libjpeg is a library for handling the JPEG (JFIF) image format."
LICENSE ="jpeg"
SECTION = "libs"
PRIORITY = "required"

DEPENDS = "libtool-cross"
DEPENDS_virtclass-native = "libtool-native"

PR = "r7"

SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian.patch;patch=1 \
	   file://ldflags.patch;patch=1 \
	   file://paths.patch;patch=1 \
	   file://libtool_tweak.patch;patch=1"

inherit autotools 

EXTRA_OECONF="--enable-static --enable-shared"
EXTRA_OEMAKE='"LIBTOOL=${STAGING_BINDIR_CROSS}/${HOST_SYS}-libtool"'

CFLAGS_append = " -D_REENTRANT"

do_configure_prepend () {
	rm -f ${S}/ltconfig
	rm -f ${S}/ltmain.sh
}

do_install() {
	install -d ${D}${bindir} ${D}${includedir} \
		   ${D}${mandir}/man1 ${D}${libdir}
	oe_runmake 'DESTDIR=${D}' install
}

PACKAGES =+ 		"jpeg-tools "
FILES_jpeg-tools = 	"${bindir}/*"

BBCLASSEXTEND = "native"
