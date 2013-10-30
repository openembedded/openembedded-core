require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=f69a4626e9efc40fa0d3cc3b02c9eacf"

PR = "${INC_PR}.0"
SRCREV = "8f0742051e8501e737affb392996aef172034ca8"
PV = "9.2.2+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0006-fix-out-of-tree-egl.patch \
           "

S = "${WORKDIR}/git"
