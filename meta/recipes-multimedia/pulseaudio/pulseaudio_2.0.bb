require pulseaudio.inc

PR = "r0"

DEPENDS += "libjson gdbm speex libxml-parser-perl-native"

inherit gettext perlnative

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/pulseaudio-${PV}.tar.xz \
  file://volatiles.04_pulse \
"

SRC_URI[md5sum] = "9bbde657c353fe675c3b693054175a8e"
SRC_URI[sha256sum] = "28b42edd42f4879a6884af5f0ec11801ac001eb7582881215b36649aa37e2061"

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}

