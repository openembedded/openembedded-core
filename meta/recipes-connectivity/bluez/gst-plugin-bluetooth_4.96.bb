require bluez4.inc
require recipes-multimedia/gstreamer/gst-plugins-package.inc

PR = "r1"

DEPENDS = "bluez4 gst-plugins-base"

EXTRA_OECONF = "\
  --enable-gstreamer \
"

# clean unwanted files
do_install_append() {
	rm -rf ${D}${bindir}
	rm -rf ${D}${sbindir}
	rm -f  ${D}${libdir}/lib*
	rm -rf ${D}${sysconfdir}
	rm -rf ${D}${base_libdir}
}

FILES_${PN} = "${libdir}/gstreamer-0.10/lib*.so"
FILES_${PN}-dev += "\
  ${libdir}/gstreamer-0.10/*.la \
"

FILES_${PN}-dbg += "\
  ${libdir}/*/.debug \
"


