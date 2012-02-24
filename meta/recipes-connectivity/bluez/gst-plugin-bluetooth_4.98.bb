require bluez4.inc
require recipes-multimedia/gstreamer/gst-plugins-package.inc

SRC_URI[md5sum] = "362864b716950baa04797de735fc237b"
SRC_URI[sha256sum] = "9a5b655bada7c7a1921cb3bac83b8a32bbe49893e4c7a1377cdc1b0d35f7d233"

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


