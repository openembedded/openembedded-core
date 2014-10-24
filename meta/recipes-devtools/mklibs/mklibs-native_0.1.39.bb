SUMMARY = "Shared library optimisation tool"
DESCRIPTION = "mklibs produces cut-down shared libraries that contain only the routines required by a particular set of executables."
HOMEPAGE = "https://launchpad.net/mklibs"
SECTION = "devel"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=98d31037b13d896e33890738ef01af64"
DEPENDS = "python-native dpkg-native"

SRC_URI = "http://ftp.de.debian.org/debian/pool/main/m/mklibs/${BPN}_${PV}.tar.xz \
	file://ac_init_fix.patch\
	file://fix_STT_GNU_IFUNC.patch\
	file://sysrooted-ldso.patch \
"

SRC_URI[md5sum] = "38a579a531401eb76f4bab4ccfb774a2"
SRC_URI[sha256sum] = "fa2881ab4fe72b0504878357f1fc6b17920459b56e8a60dfb083ca28fb64e733"

inherit autotools gettext native pythonnative

do_configure_prepend() {
	sed "s+MKLIBS_VERSION+${PV}+" ${S}/configure.ac
}
