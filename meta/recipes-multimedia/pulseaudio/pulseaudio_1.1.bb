require pulseaudio.inc

PR = "r5"

DEPENDS += "libjson gdbm speex libxml-parser-perl-native"

inherit gettext perlnative

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/pulseaudio-${PV}.tar.xz \
  file://volatiles.04_pulse \
  file://pulseaudo_fix_for_x32.patch \
"

SRC_URI[md5sum] = "17d21df798cee407b769c6355fae397a"
SRC_URI[sha256sum] = "6fe531136f6ebce2d35872a2d2c914278cdc5dcdd5eea516dc52c81f9001f5ee"

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}

