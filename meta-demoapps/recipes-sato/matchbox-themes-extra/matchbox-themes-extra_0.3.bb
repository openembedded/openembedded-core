DESCRIPTION = "Matchbox window manager extra themes"
LICENSE = "GPL"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"

SRC_URI = "http://downloads.yoctoproject.org/releases/matchbox/matchbox-themes-extra/${PV}/matchbox-themes-extra-${PV}.tar.bz2"
S = "${WORKDIR}/matchbox-themes-extra-${PV}"

SRC_URI[md5sum] = "04312628f4a21f4105bce1251ea08035"
SRC_URI[sha256sum] = "98a1c8695842b0cd7f32e67b0ef9118fd0f32db5297f3f08706c706dee8fc6be"

inherit autotools  pkgconfig

# split into several packages plus one meta package
PACKAGES = "${PN} ${PN}-industrial ${PN}-expose ${PN}-mbcrystal"

ALLOW_EMPTY_${PN} = "1"
FILES_${PN} = ""
RDEPENDS_${PN} = "${PN}-industrial ${PN}-expose ${PN}-mbcrystal"

FILES_${PN}-industrial = "${datadir}/themes/Industrial \
			  ${datadir}/icons/Industrial"

FILES_${PN}-expose = "${datadir}/themes/expose \
		      ${datadir}/icons/expose"

FILES_${PN}-mbcrystal = "${datadir}/themes/mbcrystal \
			 ${datadir}/icons/mbcrystal"
