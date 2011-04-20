SUMMARY = "libjpeg is a library for handling the JPEG (JFIF) image format."
DESCRIPTION = "libjpeg contains a library for handling the JPEG (JFIF) image format, as well as related programs for accessing the libjpeg functions."
HOMEPAGE = "http://www.ijg.org/"
BUGTRACKER = ""

LICENSE ="jpeg"
LIC_FILES_CHKSUM = "file://cdjpeg.h;endline=12;md5=78fa8dbac547bb5b2a0e6457a6cfe21d \
                    file://jpeglib.h;endline=14;md5=dd06b5575519f735ec2565ed36ce62e7 \
                    file://djpeg.c;endline=13;md5=ca89254925da06fef47e4b6468233432"

SECTION = "libs"
PRIORITY = "required"

DEPENDS = "libtool-cross"
DEPENDS_virtclass-native = "libtool-native"

PR = "r1"

SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian-libjpeg7_7-1.diff;patch=1"

SRC_URI[md5sum] = "e022acbc5b36cd2cb70785f5b575661e"
SRC_URI[sha256sum] = "36e6208edec591bae8f2fc370ea4f991447badb6377a125c211ffa7b503174a7"

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
