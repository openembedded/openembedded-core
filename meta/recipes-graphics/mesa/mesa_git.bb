require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=6a23445982a7a972ac198e93cc1cb3de"

SRCREV = "f70e4d4afd16e66506ce3b055edb3b71b1464890"
PV = "10.4.3+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;branch=10.4"

S = "${WORKDIR}/git"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#ifdef MESA_EGL_NO_X11_HEADERS/#if ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
