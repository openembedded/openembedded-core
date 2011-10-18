require pulseaudio.inc

PR = "r7"

DEPENDS += "gdbm speex libxml-parser-perl-native"

inherit gettext perlnative

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/pulseaudio-${PV}.tar.gz \
  file://buildfix.patch \
  file://autoconf_version.patch \
  file://tls_m4.patch \
  file://configure_silent_rules.patch \
  file://volatiles.04_pulse \
"

SRC_URI[md5sum] = "7391205a337d1e04a9ff38025f684034"
SRC_URI[sha256sum] = "af3e84c614cb632fd1f57105489fcd5f93f906da1ce5aa9019492212031fba4e"

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}

ARM_INSTRUCTION_SET = "arm"
