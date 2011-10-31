DESCRIPTION = "Python Gstreamer bindings"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
DEPENDS = "gstreamer gst-plugins-base python-pygobject"
RDEPENDS_${PN} += "python-pygtk"
PR = "r2"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-python/gst-python-${PV}.tar.bz2 \
           file://python-path.patch"

SRC_URI[md5sum] = "31340ae3e877797a10d088a226d74b16"
SRC_URI[sha256sum] = "d5962132c84b88b54d0f63832b8dfcc383a7805827da5abc2d1d215ba86a6f92"
S = "${WORKDIR}/gst-python-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=39ff67e932b7bdfa9b78bad67151690b"

inherit autotools distutils-base pkgconfig

EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

FILES_${PN} += "${datadir}/gst-python"
FILES_${PN}-dev += "${datadir}/gst-python/0.10/defs"
FILES_${PN}-dbg += "${libdir}/${PYTHON_DIR}/site-packages/gst-0.10/gst/.debug/ ${libdir}/gstreamer-0.10/.debug/"
