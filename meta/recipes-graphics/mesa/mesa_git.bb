require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=f69a4626e9efc40fa0d3cc3b02c9eacf"

PR = "${INC_PR}.0"
SRCREV = "5a925cc5504575c22dbb7d29842d7fc5babcb5c7"
PV = "9.1.3+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0003-EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://0004-glsl-fix-builtin_compiler-cross-compilation.patch \
           "

S = "${WORKDIR}/git"
