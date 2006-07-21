LICENSE = "LGPL"
DEPENDS = "libopensync"
SRC_URI = "http://www.o-hand.com/~chris/${PN}-${PV}.tar.gz"
PR = "r1"

inherit autotools pkgconfig

# Work around opensync's broken pkgconfig usage
do_install_append () {
        mv ${D}/${STAGING_DIR}/* ${D}/${prefix}/
}

FILES_${PN} += "${datadir}/opensync/defaults/file-sync \
		${libdir}/opensync/*/*.so"
FILES_${PN}-dev += "${libdir}/opensync/*/*.la"

