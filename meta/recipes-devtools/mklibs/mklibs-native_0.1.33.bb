DESCRIPTION = "mklibs produces cut-down shared libraries that contain only the routines required by a particular set of executables."
HOMEPAGE = "https://code.launchpad.net/mklibs"
SECTION = "devel"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=98d31037b13d896e33890738ef01af64"
DEPENDS = "python-native"

PR = "r1"

SRC_URI = "http://ftp.de.debian.org/debian/pool/main/m/mklibs/${BPN}_${PV}.tar.gz \
	file://ac_init_fix.patch\
	file://fix_STT_GNU_IFUNC.patch\
	file://include-unistd.h-for-gcc47.patch \
"

SRC_URI[md5sum] = "a462d9b802164993d247c1193116d78b"
SRC_URI[sha256sum] = "706aa22ec979bc54adaf9616d278cecc2f1e8cd5faa656269c9760e8669fcc1e"

S = "${WORKDIR}/${BPN}"

inherit autotools gettext native

do_configure_prepend() {
	sed "s+MKLIBS_VERSION+${PV}+" ${S}/configure.ac
}
