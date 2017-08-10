require ${BPN}.inc

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-${PV}.tar.xz \
           file://replace_glibc_check_with_linux.patch \
           file://disable-asm-on-non-gcc.patch \
           file://0001-Use-wayland-scanner-in-the-path.patch \
           file://0002-hardware-gloat.patch \
           file://vulkan-mkdir.patch \
           file://llvm-config-version.patch \
           file://0001-ac-fix-build-after-LLVM-5.0-SVN-r300718.patch \
           file://0002-gallivm-Fix-build-against-LLVM-SVN-r302589.patch \
           file://0001-winsys-svga-drm-Include-sys-types.h.patch \
           "
SRC_URI[md5sum] = "54758bf842f9ea53c8b57cce4311b87e"
SRC_URI[sha256sum] = "0686deadde1f126b20aa67e47e8c50502043eee4ecdf60d5009ffda3cebfee50"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
