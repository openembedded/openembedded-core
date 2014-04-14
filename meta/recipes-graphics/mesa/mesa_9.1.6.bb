require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0001-Add-MESA_EGL_NO_X11_HEADERS-to-defines.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0004-glsl-fix-builtin_compiler-cross-compilation.patch \
           file://0005-llvmpipe-remove-the-power-of-two-sizeof-struct-cmd_b.patch \
           file://0005-fix-out-of-tree-builds-gallium.patch \
           file://0006-fix-out-of-tree-egl.patch \
           "

SRC_URI[md5sum] = "443a2a352667294b53d56cb1a74114e9"
SRC_URI[sha256sum] = "e632dff0acafad0a59dc208d16dedb37f7bd58f94c5d58c4b51912e41d005e3d"

S = "${WORKDIR}/Mesa-${PV}"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@base_contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        if [ -e "${D}${includedir}/EGL/eglplatform.h" ]; then
            sed -i -e 's/^#ifdef MESA_EGL_NO_X11_HEADERS/#if ${@base_contains('DISTRO_FEATURES', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
        fi
    fi
}