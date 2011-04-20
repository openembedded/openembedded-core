DESCRIPTION = "An audio Sample Rate Conversion library"
SECTION = "libs"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/samplerate.c;beginline=1;endline=17;md5=ed4dfdaa3bdf0f817ebc70cee0cccc88"
DEPENDS = "flac"
PR = "r0"

SRC_URI = "http://www.mega-nerd.com/SRC/libsamplerate-${PV}.tar.gz \
           file://libsamplerate-0.1.7-macro-quoting.patch"

SRC_URI[md5sum] = "6731a81cb0c622c483b28c0d7f90867d"
SRC_URI[sha256sum] = "78ed5d9ff1bf162c4a078f6a3e7432a537dd2f22dc58872b081fb01156027fcc"
S = "${WORKDIR}/libsamplerate-${PV}"

inherit autotools pkgconfig
