HOMEPAGE = "http://en.opensue.org/Portal:Libzypp"
DESCRIPTION  = "The ZYpp Linux Software management framework"

LICENSE  = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=11fccc94d26293d78cb4996cb17e5fa7"

inherit cmake

DEPENDS  = "rpm boost gettext curl libxml2 zlib sat-solver expat openssl udev"

S = "${WORKDIR}/git"
PV = "0.0-git${SRCPV}"
PR = "r0"

SRC_URI = "git://gitorious.org/opensuse/libzypp.git;protocol=git \
           file://no-doc.patch \
           file://rpm5.patch"

FILES_${PN} += "${libdir}/zypp ${datadir}/zypp ${datadir}/icons"
FILES_${PN}-dev += "${datadir}/cmake"
