DESCRIPTION = "LAME is an educational tool to be used for learning about MP3 encoding."
HOMEPAGE = "http://sourceforge.net/projects/lame/files/lame/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=290&atid=100290"
SECTION = "console/utils"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46bda00ffbb0ba1dac22f8d087f54d9 \
                    file://include/lame.h;beginline=1;endline=20;md5=d3ff8cae3e79c4e9da7169c7928dc3a2"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/lame/lame-${PV}.tar.gz \
           file://no-gtk1.patch"

SRC_URI[md5sum] = "8e9866ad6b570c6c95c8cba48060473f"
SRC_URI[sha256sum] = "ac3144c76617223a9be4aaa3e28a66b51bcab28141050c3af04cb06836f772c8"

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
