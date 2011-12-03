SUMMARY = "libjpeg is a library for handling the JPEG (JFIF) image format."
DESCRIPTION = "libjpeg contains a library for handling the JPEG (JFIF) image format, as well as related programs for accessing the libjpeg functions."
HOMEPAGE = "http://www.ijg.org/"
BUGTRACKER = ""

LICENSE ="BSD-3-Clause"
LIC_FILES_CHKSUM = "file://cdjpeg.h;endline=12;md5=78fa8dbac547bb5b2a0e6457a6cfe21d \
                    file://jpeglib.h;endline=14;md5=22f1e0f8fc71f6f7563146d3b210dbe5 \
                    file://djpeg.c;endline=13;md5=ca89254925da06fef47e4b6468233432"

SECTION = "libs"

DEPENDS = "libtool-cross"
DEPENDS_virtclass-native = "libtool-native"

PR = "r2"

SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian-libjpeg7_7-1.diff;"

SRC_URI[md5sum] = "a2c10c04f396a9ce72894beb18b4e1f9"
SRC_URI[sha256sum] = "edfc0b3e004b2fe58ffeeec89f96e3a3c28972c46725ec127d01edf8a1cc7c9a"

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
    if [ "$D" = "" ]; then
        if [ ! -e ${libdir}/libjpeg.so.62 ]; then
            JPEG=`find ${libdir} -type f -name libjpeg.so.\*.\*.\*`
            ln -sf `basename $JPEG` ${libdir}/libjpeg.so.62
        fi
    else
        exit 1
    fi
}
