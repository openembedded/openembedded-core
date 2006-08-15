HOMEPAGE = "http://www.x.org"
SECTION = "x11/base"
#MAINTAINER = ""
LICENSE = "MIT-X"

RPROVIDES = "virtual/xserver"
PROVIDES = "virtual/xserver"

DEPENDS = "fontconfig freetype flex-native lbxutil kbproto \ 
xf86driproto drm glproto randrproto renderproto fixesproto damageproto \
xcmiscproto xextproto xproto xf86miscproto xf86vidmodeproto xf86bigfontproto \
scrnsaverproto bigreqsproto resourceproto fontsproto inputproto \
xf86dgaproto videoproto compositeproto trapproto recordproto dmxproto \
resourceproto xineramaproto xtrans evieext libxkbfile libxfont libxau \
libfontenc libxdmcp libxxf86vm libxaw libxmu libxt libxpm libxext libx11 \
libxkbui libxxf86misc libxi libdmx libxtst libxres mesa"

REPENDS="rgb"

XORG_PN = "xorg-server"
SRC_URI = "${XORG_MIRROR}/${@bb.data.getVar('PV', d, 1)[0:7]}/src/xserver/${XORG_PN}-${PV}.tar.gz \
	file://xorg.conf"

S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-xprint ${PN}-xvfb ${PN}-utils"

FILES_${PN}-xprint = "${libdir}/X11/xserver/*/print"
FILES_${PN}-xvfb = "${bindir}/Xvfb"
FILES_${PN}-utils = "${bindir}/scanpci ${bindir}/pcitweak ${bindir}/ioport ${bindir}/in[bwl] ${bindir}/out[bwl] ${bindir}/mmap[rw] ${bindir}/gtf ${bindir}/getconfig ${bindir}/getconfig.pl"
FILES_${PN} = "${bindir} ${libdir}/X11/Options ${libdir}/X11/Cards ${libdir}/X11/getconfig ${libdir}/X11/etc ${libdir}/xserver/SecurityPolicy ${libdir}/modules ${libdir}/xorg/modules /etc/X11 "
FILES_${PN}-doc += "${libdir}/X11/doc /usr/share/X11/xkb/compiled/README.compiled"

do_stage() {
	autotools_stage_all
}

do_install_append () {
	install -d ${D}/etc/X11
	install -m0644 ${WORKDIR}/xorg.conf ${D}/etc/X11/
}
