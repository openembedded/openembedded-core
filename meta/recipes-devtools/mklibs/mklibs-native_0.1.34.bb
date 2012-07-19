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
"

SRC_URI[md5sum] = "afe0ed527ba96b8a882b5de350603007"
SRC_URI[sha256sum] = "0c13c314f5c397529f58a5a02d57d83aeb4463d6a0d80b9374c6576ec37ed39f"

inherit autotools gettext native pythonnative

do_configure_prepend() {
	sed "s+MKLIBS_VERSION+${PV}+" ${S}/configure.ac
}
