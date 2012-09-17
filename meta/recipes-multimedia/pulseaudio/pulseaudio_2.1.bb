require pulseaudio.inc

PR = "r3"

DEPENDS += "libjson gdbm speex libxml-parser-perl-native"

inherit gettext perlnative

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/pulseaudio-${PV}.tar.xz \
  file://volatiles.04_pulse \
"

SRC_URI[md5sum] = "33e85023259d530f0a763d5204e8bad9"
SRC_URI[sha256sum] = "ef6b347bf47abfb98080795f1583018ac959b4aeb4df916b879f9e1eaba0ca7f"

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}

