require ${BPN}.inc

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-${PV}.tar.xz \
           file://replace_glibc_check_with_linux.patch \
           file://disable-asm-on-non-gcc.patch \
	   file://Use-Python-3-to-execute-the-scripts.patch \
           file://0001-Use-wayland-scanner-in-the-path.patch \
           file://0002-hardware-gloat.patch \
           file://llvm-config-version.patch \
           file://0001-winsys-svga-drm-Include-sys-types.h.patch \
           file://0001-Makefile.vulkan.am-explictly-add-lib-expat-to-intel-.patch \
           file://0001-anv_icd.py-improve-reproducible-builds.patch \
           "

SRC_URI[md5sum] = "5c59b779925f504ffd0f13c7dcd29ac6"
SRC_URI[sha256sum] = "e2844a13f2d6f8f24bee65804a51c42d8dc6ae9c36cff7ee61d0940e796d64c6"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if defined(MESA_EGL_NO_X11_HEADERS) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
