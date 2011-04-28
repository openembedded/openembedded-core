SUMMARY = "Library for converting characters to X key-presses"
DESCRIPTION = "libfakekey is a simple library for converting UTF-8 characters into 'fake' X \
key-presses."
HOMEPAGE = "http://matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/libfakekey.c;endline=30;md5=602b5ccd48f64407510867f3373b448c"

DEPENDS = "libxtst"
SECTION = "x11/wm"

SRCREV = "e8c2e412ea4a417afc1f30e32cb7bdc508b1dccc"
PV = "0.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig gettext
