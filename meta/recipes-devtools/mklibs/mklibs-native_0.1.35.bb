DESCRIPTION = "mklibs produces cut-down shared libraries that contain only the routines required by a particular set of executables."
HOMEPAGE = "https://code.launchpad.net/mklibs"
SECTION = "devel"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=98d31037b13d896e33890738ef01af64"
DEPENDS = "python-native"

SRC_URI = "http://ftp.de.debian.org/debian/pool/main/m/mklibs/${BPN}_${PV}.tar.gz \
	file://ac_init_fix.patch\
	file://fix_STT_GNU_IFUNC.patch\
"

SRC_URI[md5sum] = "3d2a4bd0bbf5ba964b0a1ecdafd1ea9a"
SRC_URI[sha256sum] = "ccb1023dc1729c5a37ca6c3eca8e4bac3491116763c8820dfce8eea4845c8567"

inherit autotools gettext native pythonnative

do_configure_prepend() {
	sed "s+MKLIBS_VERSION+${PV}+" ${S}/configure.ac
}
