SECTION = "x11"

DESCRIPTION = "An open source implementation of the OpenGL spec"
HOMEPAGE = "http://mesa3d.org"
LICENSE = "MIT"

SRC_URI = "${SOURCEFORGE_MIRROR}/mesa3d/MesaLib-${PV}.tar.bz2 \
           ${SOURCEFORGE_MIRROR}/mesa3d/MesaDemos-${PV}.tar.bz2 "
S = "${WORKDIR}/Mesa-${PV}"

PROVIDES = "virtual/libgl"

inherit autotools_stage pkgconfig

EXTRA_OECONF = "--disable-glu \
                --disable-glw \
                --disable-glut \
                --enable-glx-tls"

# Multiple virtual/gl providers being built breaks staging
EXCLUDE_FROM_WORLD = "1"


