SUMMARY = "Userspace library to access USB (version 1.0)"
HOMEPAGE = "http://libusb.sf.net"
BUGTRACKER = "http://www.libusb.org/report"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

BBCLASSEXTEND = "native nativesdk"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-${PV}.tar.bz2 \
           file://no-dll.patch \
          "

SRC_URI[md5sum] = "466267889daead47674df933cea9cacb"
SRC_URI[sha256sum] = "75aeb9d59a4fdb800d329a545c2e6799f732362193b465ea198f2aa275518157"

S = "${WORKDIR}/libusb-${PV}"

inherit autotools pkgconfig

# Don't configure udev by default since it will cause a circular
# dependecy with udev package, which depends on libusb
EXTRA_OECONF = "--libdir=${base_libdir} --disable-udev"

do_install_append() {
	install -d ${D}${libdir}
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		mv ${D}${base_libdir}/pkgconfig ${D}${libdir}
	fi
}

FILES_${PN} += "${base_libdir}/*.so.*"

FILES_${PN}-dev += "${base_libdir}/*.so ${base_libdir}/*.la"
