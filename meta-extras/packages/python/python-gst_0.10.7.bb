DESCRIPTION = "Python Gstreamer bindings"
SECTION = "devel/python"
LICENSE = "LGPL"
DEPENDS = "gstreamer"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-python/gst-python-${PV}.tar.bz2 \
           file://python-path.patch;patch=1"

S = "${WORKDIR}/gst-python-${PV}"

inherit autotools distutils-base pkgconfig

EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

do_configure_prepend() {
    install -m 0644 ${S}/common/m4/*.m4 ${S}/m4/
}

do_stage() {
	autotools_stage_all
}
