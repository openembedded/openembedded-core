require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://EGL-Mutate-NativeDisplayType-depending-on-config.patch \
           file://fix-glsl-cross.patch \
           file://0001-configure-Avoid-use-of-AC_CHECK_FILE-for-cross-compi.patch \
           file://0001-llvmpipe-remove-the-power-of-two-sizeof-struct-cmd_b.patch \
           "

SRC_URI[md5sum] = "952ccd03547ed72333b64e1746cf8ada"
SRC_URI[sha256sum] = "8d5dac2202d0355bff5cfd183582ec8167d1d1227b7bb7a669acecbeaa52d766"

S = "${WORKDIR}/Mesa-${PV}"
