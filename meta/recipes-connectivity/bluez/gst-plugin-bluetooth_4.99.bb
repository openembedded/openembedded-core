require bluez4.inc
require recipes-multimedia/gstreamer/gst-plugins-package.inc

PR = "r2"

SRC_URI[md5sum] = "570aa10692ed890aa0a4297b37824912"
SRC_URI[sha256sum] = "d884b9aa5d3d9653c076b7646ca14a3e43eb84bccfe8193c49690f802bbd827c"

DEPENDS = "bluez4 gst-plugins-base"

EXTRA_OECONF = "\
  --enable-gstreamer \
"

# clean unwanted files
do_install_append() {
	rm -rf ${D}${bindir}
	rm -rf ${D}${sbindir}
	rm -f  ${D}${libdir}/lib*
	rm -rf ${D}${libdir}/pkgconfig
	rm -rf ${D}${sysconfdir}
	rm -rf ${D}${base_libdir}
	rm -rf ${D}${libdir}/bluetooth
	rm -rf ${D}${localstatedir}
  	rm -rf ${D}${libdir}/alsa-lib
  	rm -rf ${D}${datadir}
	rm -rf ${D}${includedir}
}

FILES_${PN} = "${libdir}/gstreamer-0.10/lib*.so"
FILES_${PN}-dev += "\
  ${libdir}/gstreamer-0.10/*.la \
"

FILES_${PN}-dbg += "\
  ${libdir}/*/.debug \
"


