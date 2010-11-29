DESCRIPTION = "An audio Sample Rate Conversion library"
SECTION = "libs"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/samplerate.c;beginline=1;endline=17;md5=2deac26142757e825d957e5ac149b292"
DEPENDS = "flac"
PR = "r3"

SRC_URI = "http://www.mega-nerd.com/SRC/libsamplerate-${PV}.tar.gz"
S = "${WORKDIR}/libsamplerate-${PV}"

inherit autotools pkgconfig
