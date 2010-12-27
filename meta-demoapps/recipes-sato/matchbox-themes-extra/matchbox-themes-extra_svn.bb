DESCRIPTION = "Matchbox window manager extra themes"
LICENSE = "GPL"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"
PV = "0.3+svnr${SRCPV}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=matchbox-themes-extra;proto=http"
S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig

# split into several packages plus one meta package
PACKAGES = "${PN} ${PN}-industrial ${PN}-expose ${PN}-mbcrystal ${PN}-clearlooks"

ALLOW_EMPTY_${PN} = 1
FILES_${PN} = ""
RDEPENDS_${PN} = "${PN}-industrial ${PN}-expose ${PN}-mbcrystal ${PN}-clearlooks"

FILES_${PN}-industrial = "${datadir}/themes/Industrial \
			  ${datadir}/icons/Industrial"

FILES_${PN}-expose = "${datadir}/themes/expose \
		      ${datadir}/icons/expose"

FILES_${PN}-mbcrystal = "${datadir}/themes/mbcrystal \
			 ${datadir}/icons/mbcrystal"

FILES_${PN}-clearlooks = "${datadir}/themes/Clearlooks \
			  ${datadir}/icons/Clearlooks"

