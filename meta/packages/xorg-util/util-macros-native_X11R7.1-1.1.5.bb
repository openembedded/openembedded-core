DESCRIPTION = "X autotools macros"
SECTION = "x11/libs"
LICENSE= "Xorg"

SRC_URI = "${XORG_MIRROR}/individual/util/util-macros-1.1.5.tar.bz2 \
           file://unbreak_cross_compile.patch;patch=1 "

S = "${WORKDIR}/util-macros-1.1.5"

inherit native autotools pkgconfig

