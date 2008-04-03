DESCRIPTION = "Matchbox window manager Sato themes"
LICENSE = "CC-BY-SA3"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/themes"
