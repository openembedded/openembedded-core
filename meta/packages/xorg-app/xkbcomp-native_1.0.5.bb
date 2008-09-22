DESCRIPTION = "The X Keyboard Extension essentially replaces the core protocol definition of keyboard."

SECTION = "x11/applications"
LICENSE = "MIT-X"
S="${WORKDIR}/xkbcomp-${PV}"

DEPENDS = "libx11-native libxkbfile-native"

SRC_URI = "${XORG_MIRROR}/individual/app/xkbcomp-${PV}.tar.bz2"

inherit native autotools pkgconfig
