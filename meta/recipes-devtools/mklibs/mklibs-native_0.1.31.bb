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

SRC_URI[md5sum] = "f4df0307ccbdf60070e42277513f27ed"
SRC_URI[sha256sum] = "8f5595621eb09d52871c771861e81b032d10c31d15e5dd61fa7f5a9e5b7de405"

S = "${WORKDIR}/${BPN}"

inherit autotools gettext native

do_configure_prepend() {
	sed "s+MKLIBS_VERSION+${PV}+" ${S}/configure.ac
}
