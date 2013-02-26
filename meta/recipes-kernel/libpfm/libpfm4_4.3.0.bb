SUMMARY = "Monitoring tools exploiting the performance monitoring events"
DESCRIPTION = "This package provides a library, called libpfm4 which is used to develop \
monitoring tools exploiting the performance monitoring events such as those \
provided by the Performance Monitoring Unit (PMU) of modern processors."
HOMEPAGE = "http://perfmon2.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=144822&atid=759953&source=navbar"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=0de488f3bd4424e308e2e399cb99c788"

SECTION = "devel"

PR = "r1"
COMPATIBLE_HOST = "powerpc64"

SRC_URI = "http://downloads.sourceforge.net/project/perfmon2/libpfm4/libpfm-4.3.0.tar.gz"

SRC_URI[md5sum] = "0ab272dbdbb759b852ba8bd06db030ef"
SRC_URI[sha256sum] = "a23eb9affbff279e13563a39317c0ad71c4de28908d4243c8bc109138430cc3b"

EXTRA_OEMAKE = "DESTDIR=\"${D}\" PREFIX=\"${prefix}\" LIBDIR=\"${libdir}\" LDCONFIG=\"true\""
EXTRA_OEMAKE_append_powerpc = " ARCH=\"powerpc\""
EXTRA_OEMAKE_append_powerpc64 = " ARCH=\"powerpc\" BITMODE=\"64\""

S = "${WORKDIR}/libpfm-${PV}"

do_install () {
	oe_runmake install
}
