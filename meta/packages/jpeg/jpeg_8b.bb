DESCRIPTION = "libjpeg is a library for handling the JPEG (JFIF) image format."
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

PR = "r0"

SRC_URI = "http://www.ijg.org/files/jpegsrc.v${PV}.tar.gz \
	   file://debian-libjpeg7_7-1.diff;patch=1"

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
