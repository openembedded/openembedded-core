DESCRIPTION = "Python Gstreamer bindings"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
DEPENDS = "gstreamer gst-plugins-base python-pygobject"
RDEPENDS_${PN} += "python-pygtk"
PR = "r1"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-python/gst-python-${PV}.tar.bz2 \
           file://python-path.patch"

SRC_URI[md5sum] = "ddcef7d00bd88b0591cd6d910c36ec4b"
SRC_URI[sha256sum] = "956f81a8c15daa3f17e688a0dc5a5d18a3118141066952d3b201a6ac0c52b415"
S = "${WORKDIR}/gst-python-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=39ff67e932b7bdfa9b78bad67151690b"

inherit autotools distutils-base pkgconfig

EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

FILES_${PN} += "${datadir}/gst-python"
FILES_${PN}-dev += "${datadir}/gst-python/0.10/defs"
FILES_${PN}-dbg += "${libdir}/${PYTHON_DIR}/site-packages/gst-0.10/gst/.debug/"
