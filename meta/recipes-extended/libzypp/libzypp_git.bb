HOMEPAGE = "http://en.opensue.org/Portal:Libzypp"
DESCRIPTION  = "The ZYpp Linux Software management framework"

LICENSE  = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=11fccc94d26293d78cb4996cb17e5fa7"

inherit cmake

DEPENDS  = "rpm boost gettext curl libxml2 zlib sat-solver expat openssl udev"

# rpmdb2solv from sat-solver is run from libzypp
RDEPENDS_${PN} = "sat-solver"

S = "${WORKDIR}/git"
PV = "0.0-git${SRCPV}"
PR = "r4"

SRC_URI = "git://gitorious.org/opensuse/libzypp.git;protocol=git \
           file://no-doc.patch \
           file://rpm5.patch"

SRC_URI_append_mips = " file://mips-workaround-gcc-tribool-error.patch"

FILES_${PN} += "${libdir}/zypp ${datadir}/zypp ${datadir}/icons"
FILES_${PN}-dev += "${datadir}/cmake"

EXTRA_OECMAKE += "-DLIB=lib"
