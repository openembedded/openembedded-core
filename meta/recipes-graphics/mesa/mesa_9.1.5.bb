require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0003-EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://0004-glsl-fix-builtin_compiler-cross-compilation.patch \
           file://0005-llvmpipe-remove-the-power-of-two-sizeof-struct-cmd_b.patch \
           "

SRC_URI[md5sum] = "4ed2af5943141a85a21869053a2fc2eb"
SRC_URI[sha256sum] = "89ea0d1afd90a87dab253777bfe414820c4aa3890add1487ca2b49f6b3e194fd"

S = "${WORKDIR}/Mesa-${PV}"
