require pulseaudio.inc

PR = "r5"

DEPENDS += "gdbm speex"

inherit gettext

SRC_URI += "\
  file://buildfix.patch \
  file://autoconf_version.patch \
  file://tls_m4.patch \
  file://configure_silent_rules.patch \
  file://armv4+v5asm.patch \
"

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}

SRC_URI[md5sum] = "ca85ab470669b05e100861654cf5eb3c"
SRC_URI[sha256sum] = "c6019324395117a258c048a6db5e9734551cc2c61dc35b46403ff00d64be55f0"
