SECTION = "x11"

DESCRIPTION = "An open source implementation of the OpenGL spec"
HOMEPAGE = "http://mesa3d.org"
LICENSE = "MIT"

SRC_URI = "${SOURCEFORGE_MIRROR}/mesa3d/MesaLib-${PV}.tar.bz2"
S = "${WORKDIR}/Mesa-${PV}"

PROVIDES = "virtual/libgl"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-glu \
                --disable-glw \
                --disable-glut \
                --enable-glx-tls"

do_stage() {
        autotools_stage_all
}


