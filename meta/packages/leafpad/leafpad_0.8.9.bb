LICENSE = "GPLv2"
DEPENDS = "gtk+ intltool-native"
SRC_URI = "http://savannah.nongnu.org/download/${PN}/${PN}-${PV}.tar.gz \
	   file://leafpad.desktop"
PR = "r2"

inherit autotools pkgconfig

EXTRA_OECONF = " --enable-chooser --disable-gtktest --disable-print"

do_install_append () {
        install -d ${D}/${datadir}
        install -d ${D}/${datadir}/applications

        install -m 0644 ${WORKDIR}/leafpad.desktop ${D}/${datadir}/applications
}

FILES_${PN} += "${datadir}/applications/leafpad.desktop"
