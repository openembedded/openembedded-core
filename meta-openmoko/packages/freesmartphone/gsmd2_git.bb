DESCRIPTION = "GSM 07.07 phone server"
HOMEPAGE = "http://www.freesmartphone.org/mediawiki/index.php/Implementations/gsm0710muxd"
AUTHOR = "Ixonos Team"
SECTION = "console/network"
DEPENDS = "dbus-native dbus dbus-glib"
RDEPENDS = "gsm0710muxd"
LICENSE = "GPL"
PV = "0.1.0+gitr${SRCREV}"
PR = "r3"

SRC_URI = "${FREESMARTPHONE_GIT}/gsmd2.git;protocol=git;branch=master \
           file://fix-dbus-location.patch;patch=1"

S = "${WORKDIR}/git"

inherit autotools

EXTRA_OECONF = "--disable-tests"

do_install_append () {
        install -d ${D}${sysconfdir}/gsmd2

        install -d ${D}${sysconfdir}/dbus-1/system.d/
        install -m 0644 ${S}/res/freesmartphone.conf ${D}${sysconfdir}/dbus-1/system.d/

        install -d ${D}${datadir}/dbus-1/system-services
        install -m 0644 ${S}/gsmd2.service ${D}${datadir}/dbus-1/system-services/
}

PACKAGES =+ "libgsmd2 libfreesmartphone"

FILES_libgsmd2 = "${libdir}/libgsmd2.so.* ${libdir}/gsmd2/*.so"
FILES_libfreesmartphone = "${libdir}/libfreesmartphone.so.*"
FILES_${PN}-dev += "${libdir}/gsmd2/*.*a"
FILES_${PN} = "${bindir}/gsmd2 ${sysconfdir}/dbus-1/ ${datadir}/dbus-1/"
