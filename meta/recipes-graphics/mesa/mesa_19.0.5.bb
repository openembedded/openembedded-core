require ${BPN}.inc

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-${PV}.tar.xz \
           file://0001-meson.build-check-for-all-linux-host_os-combinations.patch \
           file://0001-meson.build-make-TLS-GLX-optional-again.patch \
           file://0001-Allow-enable-DRI-without-DRI-drivers.patch \
           "

SRC_URI[md5sum] = "1509a3251d459fd8b6fadf9329669dc1"
SRC_URI[sha256sum] = "6aecb7f67c136768692fb3c33a54196186c6c4fcafab7973516a355e1a54f831"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
