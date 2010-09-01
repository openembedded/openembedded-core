DESCRIPTION = "userspace library to access USB (version 1.0)"
HOMEPAGE = "http://libusb.sf.net"
BUGTRACKER = "http://www.libusb.org/report"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-${PV}.tar.bz2"
S = "${WORKDIR}/libusb-${PV}"

inherit autotools pkgconfig
