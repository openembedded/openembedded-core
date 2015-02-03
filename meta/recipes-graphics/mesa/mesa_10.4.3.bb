require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"

SRC_URI[md5sum] = "be86c4ee226f8ca28b7d4c728c350b01"
SRC_URI[sha256sum] = "ef6ecc9c2f36c9f78d1662382a69ae961f38f03af3a0c3268e53f351aa1978ad"

S = "${WORKDIR}/Mesa-${PV}"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#ifdef MESA_EGL_NO_X11_HEADERS/#if ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
