DESCRIPTION = "libusb is a library to provide userspace \
access to USB devices."
HOMEPAGE = "http://libusb.sf.net"
SECTION = "libs"
LICENSE = "LGPL"
PR = "r3"

SRC_URI = "${SOURCEFORGE_MIRROR}/libusb/libusb-${PV}.tar.gz"
#           file://configure_fix.patch;patch=1

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libusb"

S = "${WORKDIR}/libusb-${PV}"

inherit autotools pkgconfig binconfig lib_package

PARALLEL_MAKE = ""
EXTRA_OECONF = "--disable-build-docs"

export CXXFLAGS += "-lstdc++"

do_stage() {

	autotools_stage_all
	install -m 755 ${S}/libusb-config ${STAGING_BINDIR}
	# can we get rid of that? wouldn't a sed statement do as well?
	sed -i 's:\-L${libdir} :-L${STAGING_LIBDIR} :' ${STAGING_BINDIR}/libusb-config

	if [ "${STAGING_BINDIR}" != "${STAGING_BINDIR_CROSS}" ]; then
	        install -d ${STAGING_BINDIR_CROSS}/
		mv ${STAGING_BINDIR}/libusb-config ${STAGING_BINDIR_CROSS}/libusb-config
	fi

}

PACKAGES =+ "libusbpp"

FILES_libusbpp = "${libdir}/libusbpp*.so.*"
