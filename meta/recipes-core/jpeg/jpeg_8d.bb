SUMMARY = "libjpeg is a library for handling the JPEG (JFIF) image format"
DESCRIPTION = "libjpeg contains a library for handling the JPEG (JFIF) image format, as well as related programs for accessing the libjpeg functions."
HOMEPAGE = "http://www.ijg.org/"

LICENSE ="BSD-3-Clause"
LIC_FILES_CHKSUM = "file://README;md5=4f46756b064c225fae088903300e5c98"

SECTION = "libs"

DEPENDS = "libtool-cross"
DEPENDS_class-native = "libtool-native"

PR = "r1"

SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian-libjpeg7_7-1.diff \
           file://fix_for_automake_1.12.1.patch"

SRC_URI[md5sum] = "52654eb3b2e60c35731ea8fc87f1bd29"
SRC_URI[sha256sum] = "00029b1473f0f0ea72fbca3230e8cb25797fbb27e58ae2e46bb8bf5a806fe0b3"

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
DESCRIPTION_jpeg-tools = "The jpeg-tools package includes the client programs for access libjpeg functionality.  These tools allow for the compression, decompression, transformation and display of JPEG files."
FILES_jpeg-tools = 	"${bindir}/*"

BBCLASSEXTEND = "native"

pkg_postinst_${PN}_linuxstdbase () {
    if [ ! -e $D${libdir}/libjpeg.so.62 ]; then
        JPEG=`find $D${libdir} -type f -name libjpeg.so.\*.\*.\*`
        ln -sf `basename $JPEG` $D${libdir}/libjpeg.so.62
    fi
}
