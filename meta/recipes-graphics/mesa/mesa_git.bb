require ${BPN}.inc

DEFAULT_PREFERENCE = "-1"

PR = "${INC_PR}.0"
# 9.1.3 commit
SRCREV = "f32ec82a8cfcabc5b7596796f36afe7986651f02"
PV = "9.1.3+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://fix-glsl-cross.patch \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0001-llvmpipe-remove-the-power-of-two-sizeof-struct-cmd_b.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           "

S = "${WORKDIR}/git"
