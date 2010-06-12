DESCRIPTION = "libusb is a library to provide userspace \
access to USB devices. This is 0.1 legacy version"
HOMEPAGE = "http://www.libusb.org"
SECTION = "libs"

# usb.h.in and usb.h.in are under dual license of LGPL|BSD
# COPYING is presented as LGPLv2.1+ but not used

LICENSE = "LGPLv2+ & ( LGPLv2+ | BSD )"
LICENSE_libusb = "LGPLv2+"
LICENSE_libusb-dev = "LGPLv2+ | BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f2ac5f3ac4835e8f91324a26a590a423"
PR = "r5"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-${PV}.tar.gz \
           file://configure_fix.patch;apply=yes"

inherit autotools pkgconfig binconfig lib_package

PARALLEL_MAKE = ""
EXTRA_OECONF = "--disable-build-docs"

export CXXFLAGS += "-lstdc++"

PACKAGES =+ "libusbpp"

FILES_libusbpp = "${libdir}/libusbpp*.so.*"

BBCLASSEXTEND = "native"
