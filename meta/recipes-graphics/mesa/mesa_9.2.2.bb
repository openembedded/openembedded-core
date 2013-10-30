require ${BPN}.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0002-pipe_loader_sw-include-xlib_sw_winsys.h-only-when-HA.patch \
           file://0006-fix-out-of-tree-egl.patch \
           "

SRC_URI[md5sum] = "20887f8020db7d1736a01ae9cd5d8c38"
SRC_URI[sha256sum] = "c78a5035233672844cf2492fe82dc10877e12026be227d04469d6ca6ac656a3d"

S = "${WORKDIR}/Mesa-${PV}"
