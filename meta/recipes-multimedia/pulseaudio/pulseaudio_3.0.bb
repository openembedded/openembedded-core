require pulseaudio.inc

PR = "r0"

DEPENDS += "libjson gdbm speex libxml-parser-perl-native"

inherit gettext perlnative

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/pulseaudio-${PV}.tar.xz \
           file://volatiles.04_pulse"

SRC_URI[md5sum] = "47fd7eca8479c757822bee68a1feef25"
SRC_URI[sha256sum] = "c90bfda29605942d08e3e218ef10e3c660506a06651a616bfbb6a6df8392836d"

do_compile_prepend() {
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}

