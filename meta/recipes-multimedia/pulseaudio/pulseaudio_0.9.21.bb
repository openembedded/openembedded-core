require pulseaudio.inc

PR = "r0"

DEPENDS += "gdbm speex"

DEFAULT_PREFERENCE_om-gta01 = "-1"
DEFAULT_PREFERENCE_om-gta02 = "-1"
DEFAULT_PREFERENCE_motorola-ezx = "-1"

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

SRC_URI[md5sum] = "76e623c4c72e2258bc8bdeb1599bad74"
SRC_URI[sha256sum] = "94e22356ac70ee95388ae58da90d88c6b3186d1938618d21671627ff56cee254"
