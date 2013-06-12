# 9.1.3 commit
SRCREV = "f32ec82a8cfcabc5b7596796f36afe7986651f02"
PV = "9.1.3+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://fix-glsl-cross.patch \
           "

S = "${WORKDIR}/git"
