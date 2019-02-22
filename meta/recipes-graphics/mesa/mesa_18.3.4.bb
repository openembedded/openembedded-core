require ${BPN}.inc

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-${PV}.tar.xz \
           file://0001-Simplify-wayland-scanner-lookup.patch \
           file://0002-winsys-svga-drm-Include-sys-types.h.patch \
           file://0003-Properly-get-LLVM-version-when-using-LLVM-Git-releas.patch \
           file://0004-use-PKG_CHECK_VAR-for-defining-WAYLAND_PROTOCOLS_DAT.patch \
           file://0005-egl-add-missing-include-stddef.h-in-egldevice.h.patch \
"

SRC_URI[md5sum] = "6f2a5e01dd5cb91d05a9534f5a80c35d"
SRC_URI[sha256sum] = "32314da4365d37f80d84f599bd9625b00161c273c39600ba63b45002d500bb07"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
