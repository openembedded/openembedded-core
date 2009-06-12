DESCRIPTION = "An audio Sample Rate Conversion library"
SECTION = "libs"
LICENSE = "GPL libsamplerate"
DEPENDS = "flac"
PR = "r3"

SRC_URI = "http://www.mega-nerd.com/SRC/libsamplerate-${PV}.tar.gz"
S = "${WORKDIR}/libsamplerate-${PV}"

inherit autotools_stage pkgconfig
