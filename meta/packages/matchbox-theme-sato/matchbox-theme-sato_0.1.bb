DESCRIPTION = "Matchbox window manager Sato themes"
LICENSE = "CC-BY-SA3"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"

SRC_URI = "http://pokylinux.org/releases/sato/matchbox-theme-sato-0.1.tar.gz"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/themes"

