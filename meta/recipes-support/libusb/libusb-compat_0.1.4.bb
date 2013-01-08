DESCRIPTION = "libusb-0.1 compatible layer for libusb1, a drop-in replacement \
that aims to look, feel and behave exactly like libusb-0.1"
HOMEPAGE = "http://www.libusb.org/"
BUGTRACKER = "http://www.libusb.org/report"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f2ac5f3ac4835e8f91324a26a590a423"
DEPENDS = "libusb1"

# Few packages are known not to work with libusb-compat (e.g. libmtp-1.0.0),
# so here libusb-0.1 is removed completely instead of adding virtual/libusb0.
# Besides, libusb-0.1 uses a per 1ms polling that hurts a lot to power
# consumption.
PROVIDES = "libusb virtual/libusb0"
BBCLASSEXTEND = "native nativesdk"

PE = "1"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-compat-${PV}.tar.bz2 \
           file://obsolete_automake_macros.patch \
          "
#           file://0.1.0-beta1-gcc3.4-fix.patch"

SRC_URI[md5sum] = "2ca521fffadd0c28fdf174e6ec73865b"
SRC_URI[sha256sum] = "ed5bdd160c7b01ef767fb931a81b454f46226d1e2cf58502ced758d3e5a9fdc4"

inherit autotools pkgconfig binconfig lib_package

EXTRA_OECONF = "--libdir=${base_libdir}"

do_install_append() {
	install -d ${D}${libdir}
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		mv ${D}${base_libdir}/pkgconfig ${D}${libdir}
	fi
}
