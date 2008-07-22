DESCRIPTION = "GSM 07.07 phone server"
HOMEPAGE = "http://www.freesmartphone.org/mediawiki/index.php/Implementations/gsm0710muxd"
AUTHOR = "Ixonos Team"
SECTION = "console/network"
DEPENDS = "dbus-native dbus dbus-glib"
RDEPENDS = "gsm0710muxd"
LICENSE = "GPL"
PV = "0.1.0+gitr${SRCREV}"
PR = "r1"

SRC_URI = "${FREESMARTPHONE_GIT}/gsmd2.git;protocol=git;branch=master \
           file://fix-dbus-location.patch;patch=1"

S = "${WORKDIR}/git"

inherit autotools

EXTRA_OECONF = "--disable-tests"

PACKAGES =+ "libgsmd2 libfreesmartphone"

FILES_libgsmd2 = "${libdir}/libgsmd2.so.* ${libdir}/gsmd2/*.so"
FILES_libfreesmartphone = "${libdir}/libfreesmartphone.so.*"
FILES_${PN}-dev += "${libdir}/gsmd2/*.*a"
FILES_${PN} = "${bindir}/gsmd2"
