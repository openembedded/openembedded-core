SUMMARY = "Library for converting characters to X key-presses"
DESCRIPTION = "libfakekey is a simple library for converting UTF-8 characters into 'fake' X \
key-presses."
HOMEPAGE = "http://matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/libfakekey.c;endline=30;md5=602b5ccd48f64407510867f3373b448c"

DEPENDS = "libxtst"
SECTION = "x11/wm"
PV = "0.0+svnr${SRCPV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext
