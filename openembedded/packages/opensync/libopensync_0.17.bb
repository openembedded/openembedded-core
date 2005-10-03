LICENSE = "LGPL"
DEPENDS = "sqlite3"
SRC_URI = "http://www.o-hand.com/~chris/${PN}-${PV}.tar.gz"
EXTRA_OECONF = " --enable-engine"

inherit autotools pkgconfig

do_stage () {
	oe_libinstall -so -C opensync libopensync ${STAGING_LIBDIR}
	oe_libinstall -so -C osengine libosengine ${STAGING_LIBDIR}

	install -d ${STAGING_INCDIR}/opensync-1.0 \
		   ${STAGING_INCDIR}/opensync-1.0/opensync \
		   ${STAGING_INCDIR}/opensync-1.0/osengine
	install -m 0644 ${S}/opensync/*.h ${STAGING_INCDIR}/opensync-1.0/opensync
	install -m 0644 ${S}/osengine/*.h ${STAGING_INCDIR}/opensync-1.0/osengine

	install -m 0644 ${S}/opensync-1.0.pc ${STAGING_LIBDIR}/pkgconfig
	install -m 0644 ${S}/osengine-1.0.pc ${STAGING_LIBDIR}/pkgconfig
}

FILES_${PN} += "${libdir}/opensync/formats/*.so"
FILES_${PN}-dev += "${libdir}/opensync/formats/*.la"

