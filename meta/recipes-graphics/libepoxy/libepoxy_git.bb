SUMMARY = "OpenGL function pointer management library"
HOMEPAGE = "https://github.com/anholt/libepoxy/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=58ef4c80d401e07bd9ee8b6b58cf464b"


SRC_URI = " \
    git://github.com/anholt/libepoxy.git \
    file://0001-select-platforms-based-on-configuration-results.patch \
    file://0002-add-an-option-to-disable-glx-support.patch \
"
SRCREV="20062c25e7612cab023cdef44d3277ba1bd0b2de"
PV = "1.2+git${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

DEPENDS = "util-macros virtual/egl"

PACKAGECONFIG[x11] = "--enable-glx, --disable-glx, virtual/libx11"
PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
