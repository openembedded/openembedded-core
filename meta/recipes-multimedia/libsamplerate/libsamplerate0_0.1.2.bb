DESCRIPTION = "An audio Sample Rate Conversion library"
SECTION = "libs"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/samplerate.c;beginline=1;endline=17;md5=2deac26142757e825d957e5ac149b292"
DEPENDS = "flac"
PR = "r3"

SRC_URI = "http://www.mega-nerd.com/SRC/libsamplerate-${PV}.tar.gz"

SRC_URI[md5sum] = "06861c2c6b8e5273c9b80cf736b9fd0e"
SRC_URI[sha256sum] = "98b8766323c78b7b718dfd4ef6b9292bbf0796b742abb2319b8278cbeee731d4"
S = "${WORKDIR}/libsamplerate-${PV}"

inherit autotools pkgconfig
