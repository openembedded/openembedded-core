require pulseaudio.inc

DEPENDS += "dbus gdbm speex"
PR = "r1"

SRC_URI += "\
  file://buildfix.patch;patch=1 \
  file://autoconf_version.patch;patch=1 \
  file://2113.diff;patch=1;pnum=0 \
  file://2114.diff;patch=1;pnum=0 \
"            

do_compile_prepend() {
    cd ${S}
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}
