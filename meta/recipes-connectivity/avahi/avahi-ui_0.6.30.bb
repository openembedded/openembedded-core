LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://avahi-common/address.h;endline=25;md5=b1d1d2cda1c07eb848ea7d6215712d9d \
                    file://avahi-core/dns.h;endline=23;md5=6fe82590b81aa0ddea5095b548e2fdcb \
                    file://avahi-daemon/main.c;endline=21;md5=9ee77368c5407af77caaef1b07285969 \
                    file://avahi-client/client.h;endline=23;md5=f4ac741a25c4f434039ba3e18c8674cf"

require avahi.inc

PR = "${INC_PR}.2"

DEPENDS += "avahi gtk+ libglade"

AVAHI_GTK = "--enable-gtk --disable-gtk3"

S = "${WORKDIR}/avahi-${PV}"

PACKAGES = "${PN} ${PN}-utils ${PN}-dbg ${PN}-dev ${PN}-doc python-avahi avahi-discover avahi-discover-standalone"

FILES_${PN} = "${libdir}/libavahi-ui*.so.*"
FILES_${PN}-dbg += "${libdir}/.debug/libavah-ui*"
FILES_${PN}-dev += "${libdir}/libavahi-ui*"

FILES_${PN}-utils = "${bindir}/b* ${datadir}/applications/b*"

FILES_python-avahi = "${PYTHON_SITEPACKAGES_DIR}/avahi/*"
FILES_avahi-discover = "${bindir}/avahi-discover \
                        ${datadir}/applications/avahi-discover.desktop \
                        ${datadir}/avahi/interfaces/avahi-discover*"
FILES_avahi-discover-standalone = "${bindir}/avahi-discover-standalone \
                                   ${datadir}/avahi/interfaces/avahi-discover.glade"

RDEPENDS_avahi-discover = "python-avahi python-pygtk"
RDEPENDS_python-avahi = "python-dbus"


SRC_URI[md5sum] = "e4db89a2a403ff4c47d66ac66fad1f43"
SRC_URI[sha256sum] = "f9e4316c2339d0020726edd846d01bee0c39980906db0c247479e5807457ff1f"

do_install_append () {
	rm ${D}${sysconfdir} -rf
	rm ${D}${base_libdir} -rf
	rm ${D}${base_libdir} -rf
	rm ${D}${bindir}/avahi-b*
	rm ${D}${bindir}/avahi-p*
	rm ${D}${bindir}/avahi-r*
	rm ${D}${bindir}/avahi-s*
	rm ${D}${includedir}/avahi-c* -rf
	rm ${D}${includedir}/avahi-g* -rf
	rm ${D}${libdir}/libavahi-c*
	rm ${D}${libdir}/libavahi-g*
	rm ${D}${libdir}/pkgconfig/avahi-c*
	rm ${D}${libdir}/pkgconfig/avahi-g*
	rm ${D}${sbindir} -rf
	rm ${D}${datadir}/avahi/a*
	rm ${D}${datadir}/avahi/s*
	rm ${D}${datadir}/dbus* -rf
	rm ${D}${mandir}/man1/a*
	rm ${D}${mandir}/man5 -rf
	rm ${D}${mandir}/man8 -rf
	rmdir ${D}${localstatedir}
}

