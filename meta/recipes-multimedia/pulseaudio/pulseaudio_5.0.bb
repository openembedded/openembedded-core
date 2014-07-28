require pulseaudio.inc

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/pulseaudio-${PV}.tar.xz \
           file://0001-configure.ac-Check-only-for-libsystemd-not-libsystem.patch \
           file://volatiles.04_pulse \
           file://CVE-2014-3970.patch \
"
SRC_URI[md5sum] = "c43749838612f4860465e83ed62ca38e"
SRC_URI[sha256sum] = "99c13a8b1249ddbd724f195579df79484e9af6418cecf6a15f003a7f36caf939"

do_compile_prepend() {
    mkdir -p ${S}/libltdl
    cp ${STAGING_LIBDIR}/libltdl* ${S}/libltdl
}
