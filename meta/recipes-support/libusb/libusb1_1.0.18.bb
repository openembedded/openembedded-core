SUMMARY = "Userspace library to access USB (version 1.0)"
HOMEPAGE = "http://libusb.sf.net"
BUGTRACKER = "http://www.libusb.org/report"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

BBCLASSEXTEND = "native nativesdk"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-${PV}.tar.bz2 \
          "

SRC_URI[md5sum] = "4a6d049923efc6496f1d383054dac1a6"
SRC_URI[sha256sum] = "c73f5cec45a5de94418da4e151b7232958571926984acfb9bce02b9424e83720"

S = "${WORKDIR}/libusb-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--libdir=${base_libdir}"

do_install_append() {
	install -d ${D}${libdir}
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		mv ${D}${base_libdir}/pkgconfig ${D}${libdir}
	fi
}

FILES_${PN} += "${base_libdir}/*.so.*"

FILES_${PN}-dev += "${base_libdir}/*.so ${base_libdir}/*.la"
