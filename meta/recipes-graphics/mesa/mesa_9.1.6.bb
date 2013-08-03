require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0003-EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://0004-glsl-fix-builtin_compiler-cross-compilation.patch \
           file://0005-llvmpipe-remove-the-power-of-two-sizeof-struct-cmd_b.patch \
           "

SRC_URI[md5sum] = "443a2a352667294b53d56cb1a74114e9"
SRC_URI[sha256sum] = "e632dff0acafad0a59dc208d16dedb37f7bd58f94c5d58c4b51912e41d005e3d"

S = "${WORKDIR}/Mesa-${PV}"
