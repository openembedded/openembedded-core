require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/mesa-${PV}.tar.xz \
           file://replace_glibc_check_with_linux.patch \
           file://disable-asm-on-non-gcc.patch \
           file://0001-Use-wayland-scanner-in-the-path.patch \
"

SRC_URI[md5sum] = "4a16cfc1c6d034cc17314b866eada628"
SRC_URI[sha256sum] = "1269dc8545a193932a0779b2db5bce9be4a5f6813b98c38b93b372be8362a346"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
