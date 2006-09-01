SECTION = "console/utils"
DESCRIPTION = "Not an MP3 encoder"
LICENSE = "LGPL"
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/lame/lame-${PV}.tar.gz \
	file://no-gtk1.patch;patch=1"

inherit autotools

PACKAGES += "libmp3lame libmp3lame-dev"
FILES_${PN} = "${bindir}/lame"
FILES_libmp3lame = "${libdir}/libmp3lame.so.*"
FILES_libmp3lame-dev = "${includedir} ${libdir}"
FILES_${PN}-dev = ""

do_configure() {
	# no autoreconf please
	aclocal
	autoconf
	libtoolize --force
	oe_runconf
}

do_stage() {
	install -d ${STAGING_LIBDIR}
	oe_libinstall -C libmp3lame -so -a libmp3lame ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/lame
	install -m 0644 include/lame.h ${STAGING_INCDIR}/lame/
}
