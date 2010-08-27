DEPENDS = "gstreamer"
RDEPENDS = "libomxil"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-openmax/pre/gst-openmax-0.10.0.2.tar.bz2"

inherit autotools

EXTRA_OECONF += "--disable-valgrind"

do_configure_prepend() {

    install -d ${S}/m4/
    install -m 0644 ${S}/common/m4/*.m4 ${S}/m4/
}

FILES_${PN} += "${libdir}/gstreamer-0.10/libgstomx.so"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/libgstomx.*a"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug/"
