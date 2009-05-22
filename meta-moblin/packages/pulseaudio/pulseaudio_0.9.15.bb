require pulseaudio.inc

DEPENDS += "dbus gdbm speex"
PR = "r6"

SRC_URI += "\
  file://buildfix.patch;patch=1 \
  file://autoconf_version.patch;patch=1 \
"

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}
