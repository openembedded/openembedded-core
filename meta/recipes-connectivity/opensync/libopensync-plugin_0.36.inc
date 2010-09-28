DEPENDS = "libopensync (>= 0.36)"

DESCRIPTION ?= "OpenSync plugin"

SRC_URI = "http://opensync.org/download/releases/${PV}/${P}.tar.bz2"

inherit cmake

FILES_${PN} += "${libdir}/opensync*/plugins/*.so \
                ${libdir}/opensync*/formats/*.so \
                ${datadir}/opensync*/defaults/"
