DESCRIPTION = "The X.Org X server"
HOMEPAGE = "http://www.x.org"
SECTION = "x11/base"
LICENSE = "MIT-X"

# xf86-*-* packages depend on an X server built with the xfree86 DDX
# so we have a virtual to represent that:
PROVIDES = "virtual/xserver-xf86"

# Other packages tend to just care that there is *an* X server:
PROVIDES += "virtual/xserver"

XORG_PN = "xorg-server"
SRC_URI = "${XORG_MIRROR}/individual/xserver/${XORG_PN}-${PV}.tar.bz2 \
           file://xorg.conf"

S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-utils"

FILES_${PN}-utils = "${bindir}/scanpci ${bindir}/pcitweak ${bindir}/ioport ${bindir}/in[bwl] ${bindir}/out[bwl] ${bindir}/mmap[rw] ${bindir}/gtf ${bindir}/getconfig ${bindir}/getconfig.pl"
FILES_${PN} = "${libdir}/X11/Options ${libdir}/X11/Cards ${libdir}/X11/getconfig ${libdir}/X11/etc ${libdir}/xserver/SecurityPolicy ${libdir}/xorg/modules/*.so ${libdir}/xorg/modules/*/*.so ${libdir}/xorg/modules/*/*/*.so /etc/X11 "
FILES_${PN}-doc += "${libdir}/X11/doc /usr/share/X11/xkb/compiled/README.compiled"

FILES_${PN}-dbg += "${libdir}/xorg/modules/.debug \
                    ${libdir}/xorg/modules/*/.debug \
		    ${libdir}/xorg/modules/*/*/.debug \
		    "

CONFFILES_${PN} += "${sysconfdir}/X11/xorg.conf"

SRC_URI += "file://macro_tweak.patch;patch=1"

do_stage() {
	autotools_stage_all
}

do_install_append () {
	install -d ${D}/${sysconfdir}/X11
	install -m 0644 ${WORKDIR}/xorg.conf ${D}/${sysconfdir}/X11/
	ln -sf ${datadir}/fonts/X11 ${D}/${libdir}/X11/fonts
}

