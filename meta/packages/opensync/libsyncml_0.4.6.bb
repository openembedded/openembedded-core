DESCRIPTION = "Libsyncml is a implementation of the SyncML protocol."
HOMEPAGE = "http://libsyncml.opensync.org/"
LICENSE = "LGPL"
DEPENDS = "sed-native wbxml2 libsoup libxml2 bluez-libs openobex"

SRC_URI = "http://libsyncml.opensync.org/download/releases/${PV}/libsyncml-${PV}.tar.bz2 \
           file://build-in-src.patch;patch=1"

inherit cmake pkgconfig

do_stage() {
        autotools_stage_all
}

PACKAGES += "${PN}-tools"

FILES_${PN}-tools = "${bindir}"
FILES_${PN} = "${libdir}/*.so.*"
