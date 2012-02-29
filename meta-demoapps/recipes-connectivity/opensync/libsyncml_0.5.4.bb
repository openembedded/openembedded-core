DESCRIPTION = "Libsyncml is a implementation of the SyncML protocol."
HOMEPAGE = "http://libsyncml.opensync.org/"
LICENSE = "LGPL"
DEPENDS = "sed-native wbxml2 libsoup libxml2 bluez4 openobex libcheck"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libsyncml/0.5.4/libsyncml-0.5.4.tar.bz2 \
           file://build-in-src.patch;patch=1"

inherit cmake pkgconfig

PACKAGES += "${PN}-tools"

FILES_${PN}-tools = "${bindir}"
FILES_${PN} = "${libdir}/*.so.*"

export VERBOSE="1"
