DESCRIPTION = "Python Gstreamer bindings"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
DEPENDS = "gstreamer gst-plugins-base python-pygobject"
RDEPENDS += "python-pygtk"
PR = "r0"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-python/gst-python-${PV}.tar.bz2 \
           file://python-path.patch"
S = "${WORKDIR}/gst-python-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=39ff67e932b7bdfa9b78bad67151690b"

inherit autotools distutils-base pkgconfig

EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

FILES_${PN} += "${datadir}/gst-python"
FILES_${PN}-dev += "${datadir}/gst-python/0.10/defs"
FILES_${PN}-dbg += "${libdir}/${PYTHON_DIR}/site-packages/gst-0.10/gst/.debug/"
