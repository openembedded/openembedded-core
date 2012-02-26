DESCRIPTION = "LAME is an educational tool to be used for learning about MP3 encoding."
HOMEPAGE = "http://sourceforge.net/projects/lame/files/lame/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=290&atid=100290"
SECTION = "console/utils"
LICENSE = "LGPLv2+"
LICENSE_FLAGS = "commercial"

LIC_FILES_CHKSUM = "file://COPYING;md5=c46bda00ffbb0ba1dac22f8d087f54d9 \
                    file://include/lame.h;beginline=1;endline=20;md5=a2258182c593c398d15a48262130a92b \
"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/lame/lame-${PV}.tar.gz \
           file://no-gtk1.patch"

SRC_URI[md5sum] = "5ad31e33e70455eb3a7b79a5dd934fce"
SRC_URI[sha256sum] = "d4ea3c8d00d2cc09338425a25dbfeb4d587942cb3c83a677c09aeb1e850c74cf"

inherit autotools

PACKAGES += "libmp3lame libmp3lame-dev"
FILES_${PN} = "${bindir}/lame"
FILES_libmp3lame = "${libdir}/libmp3lame.so.*"
FILES_libmp3lame-dev = "${includedir} ${libdir}/*"
FILES_${PN}-dev = ""

do_configure() {
	# no autoreconf please
	aclocal
	autoconf
	libtoolize --force
	gnu-configize --force
	oe_runconf
}
