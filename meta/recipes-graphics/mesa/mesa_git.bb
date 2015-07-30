require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

SRCREV = "62eb27ac8bbd979796f50e253f6c786d7d791242"
PV = "10.4.4+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;branch=10.4"

S = "${WORKDIR}/git"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#ifdef MESA_EGL_NO_X11_HEADERS/#if ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
