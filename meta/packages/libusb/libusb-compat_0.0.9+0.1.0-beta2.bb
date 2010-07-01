DESCRIPTION = "libusb is a library to provide userspace \
access to USB devices."
HOMEPAGE = "http://www.libusb.org/"
BUGTRACKER = "http://www.libusb.org/report"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
DEPENDS = "libusb1"

PE = "1"
PR = "r0"


SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-compat-0.1.0-beta2.tar.bz2 \
					file://0.1.0-beta1-gcc3.4-fix.patch;patch=1 \
          "

S = "${WORKDIR}/libusb-compat-0.1.0-beta2"

inherit autotools pkgconfig binconfig lib_package

PARALLEL_MAKE = ""
EXTRA_OECONF = "--disable-build-docs"

export CXXFLAGS += "-lstdc++ -I${STAGING_INCDIR}"

PACKAGES =+ "libusbpp"

FILES_libusbpp = "${libdir}/libusbpp*.so.*"
