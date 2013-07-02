require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0003-EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://0004-glsl-fix-builtin_compiler-cross-compilation.patch \
           file://0005-llvmpipe-remove-the-power-of-two-sizeof-struct-cmd_b.patch \
           "

SRC_URI[md5sum] = "952ccd03547ed72333b64e1746cf8ada"
SRC_URI[sha256sum] = "8d5dac2202d0355bff5cfd183582ec8167d1d1227b7bb7a669acecbeaa52d766"

S = "${WORKDIR}/Mesa-${PV}"
