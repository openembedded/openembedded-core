SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0002-cross-compile.patch \
           file://cross-glsl.patch \
           file://dont-fail-if-libX11-isnt-installed.patch \
           file://EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           "

SRC_URI[md5sum] = "dc45d1192203e418163e0017640e1cfc"
SRC_URI[sha256sum] = "75bf31f26c6e7b5515e610c1005fd1be1f7eeb2c9e6859848dab879c355ad64e"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=012f69938d81b766472fd0d240df38a4"

S = "${WORKDIR}/Mesa-${PV}"
