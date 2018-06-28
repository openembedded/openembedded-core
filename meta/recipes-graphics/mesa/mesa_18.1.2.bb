require ${BPN}.inc

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-${PV}.tar.xz \
           file://0001-Makefile.vulkan.am-explictly-add-lib-expat-to-intel-.patch \
           file://0002-Simplify-wayland-scanner-lookup.patch \
           file://0003-winsys-svga-drm-Include-sys-types.h.patch \
           file://0004-hardware-gloat.patch \
           file://0005-Properly-get-LLVM-version-when-using-LLVM-Git-releas.patch \
           file://0006-Use-Python-3-to-execute-the-scripts.patch \
           file://0007-dri-i965-Add-missing-time.h-include.patch \
           file://parallel-make-race-fix.patch \
"

SRC_URI[md5sum] = "a2d4f031eb6bd6111d44d84004476918"
SRC_URI[sha256sum] = "070bf0648ba5b242d7303ceed32aed80842f4c0ba16e5acc1a650a46eadfb1f9"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
