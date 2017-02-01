require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/mesa-${PV}.tar.xz \
           file://replace_glibc_check_with_linux.patch \
           file://disable-asm-on-non-gcc.patch \
           file://0001-Use-wayland-scanner-in-the-path.patch \
"

SRC_URI[md5sum] = "d088a921e935218833a8071cb672a574"
SRC_URI[sha256sum] = "a95d7ce8f7bd5f88585e4be3144a341236d8c0fc91f6feaec59bb8ba3120e726"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
