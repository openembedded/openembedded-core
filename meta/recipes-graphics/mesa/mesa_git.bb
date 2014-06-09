require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=f69a4626e9efc40fa0d3cc3b02c9eacf"

PR = "${INC_PR}.0"
SRCREV = "0028eb1083e6adc110a23a5f02c993cda217067a"
PV = "10.1.3+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0006-fix-out-of-tree-egl.patch \
           "

S = "${WORKDIR}/git"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#ifdef MESA_EGL_NO_X11_HEADERS/#if ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
