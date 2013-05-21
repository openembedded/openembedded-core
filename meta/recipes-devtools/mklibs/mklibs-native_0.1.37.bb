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

SRC_URI[md5sum] = "099cc99d51f09ea51e58a56e62a46af7"
SRC_URI[sha256sum] = "9b2ef35618b1c2def1ca7a0a92c2b3029ef7f2080d894b6d1dbb3f313684d3a3"

inherit autotools gettext native pythonnative

do_configure_prepend() {
	sed "s+MKLIBS_VERSION+${PV}+" ${S}/configure.ac
}
