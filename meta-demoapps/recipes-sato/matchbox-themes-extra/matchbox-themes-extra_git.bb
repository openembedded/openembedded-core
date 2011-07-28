DESCRIPTION = "Matchbox window manager extra themes"
LICENSE = "GPL"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"
SRCREV = "f848f9e4e577d5b9719f05b5152df1ce59399f5c"
PV = "0.3+git${SRCPV}"

LIC_FILES_CHKSUM = "file://configure.ac;endline=7;md5=3c4e087662e37f10e469425f3a0ad225"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"
S = "${WORKDIR}/git"

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

