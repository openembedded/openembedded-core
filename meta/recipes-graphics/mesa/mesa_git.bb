require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=f69a4626e9efc40fa0d3cc3b02c9eacf"

PR = "${INC_PR}.0"
SRCREV = "5a925cc5504575c22dbb7d29842d7fc5babcb5c7"
PV = "9.1.3+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0001-Add-MESA_EGL_NO_X11_HEADERS-to-defines.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0004-glsl-fix-builtin_compiler-cross-compilation.patch \
           file://0005-fix-out-of-tree-builds-gallium.patch \
           file://0006-fix-out-of-tree-egl.patch \
           "

S = "${WORKDIR}/git"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@base_contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        if [ -e "${D}${includedir}/EGL/eglplatform.h" ]; then
            sed -i -e 's/^#ifdef MESA_EGL_NO_X11_HEADERS/#if ${@base_contains('DISTRO_FEATURES', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
        fi
    fi
}
