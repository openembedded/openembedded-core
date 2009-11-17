DESCRIPTION = "libusb is a library to provide userspace \
access to USB devices."
HOMEPAGE = "http://libusb.sf.net"
SECTION = "libs"
LICENSE = "LGPL"

PE = "1"
PR = "r0"

DEPENDS = "libusb1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-compat-0.1.0-beta2.tar.bz2 \
					file://0.1.0-beta1-gcc3.4-fix.patch;patch=1 \
          "

S = "${WORKDIR}/libusb-compat-0.1.0-beta2"

inherit autotools_stage pkgconfig binconfig lib_package

PARALLEL_MAKE = ""
EXTRA_OECONF = "--disable-build-docs"

export CXXFLAGS += "-lstdc++ -I${STAGING_INCDIR}"

PACKAGES =+ "libusbpp"

FILES_libusbpp = "${libdir}/libusbpp*.so.*"
