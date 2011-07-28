SUMMARY =       "Desktop presence framework"
DESCRIPTION =   "Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =      "http://www.galago-project.org/"
LICENSE =       "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS =       "libgalago dbus glib-2.0"

SRC_URI =       "http://www.galago-project.org/files/releases/source/${BPN}/${BPN}-${PV}.tar.gz "

SRC_URI[md5sum] = "fdb81f938f86f380b127158ebb542279"
SRC_URI[sha256sum] = "db42a0d1d0f8b069ea5ac1203207f9178f25ac1367f4910bd48547f5be1db4c2"

EXTRA_OECONF =	"--disable-binreloc --disable-check --disable-tests"

FILES_${PN} += "${datadir}/dbus-1/services/"

inherit autotools pkgconfig gettext

