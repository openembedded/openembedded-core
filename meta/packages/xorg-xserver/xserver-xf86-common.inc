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
FILES_${PN} = "${libdir}/X11/Options ${libdir}/X11/Cards ${libdir}/X11/getconfig ${libdir}/X11/etc ${libdir}/xserver/SecurityPolicy ${libdir}/xorg/modules/*.so /etc/X11 "
FILES_${PN}-doc += "${libdir}/X11/doc /usr/share/X11/xkb/compiled/README.compiled"

FILES_${PN}-dbg += "${libdir}/xorg/modules/.debug \
                    ${libdir}/xorg/modules/*/.debug \
		    ${libdir}/xorg/modules/*/*/.debug \
		    "

CONFFILES_${PN} += "${sysconfdir}/X11/xorg.conf"

# xorg-server.m4 contains a macro used by the xf86-*-* drivers to check for
# server extensions. The problem is that it modifies CFLAGS during the drivers
# ./configure with -I`pkg-config --variable=sdkdir xorg-server` and the sdkdir
# variable doesn't point into the staging area and so causes
# "CROSS COMPILE Badness" warnings.
do_configure_prepend () {
        sed -i "s|\`pkg-config --variable=sdkdir xorg-server\`|${STAGING_DIR}/${HOST_SYS}/\`pkg-config --variable=sdkdir xorg-server\`|" xorg-server.m4
}

do_stage() {
	autotools_stage_all
}

do_install_append () {
	install -d ${D}/${sysconfdir}/X11
#	install -m 0644 ${WORKDIR}/xorg.conf ${D}/${sysconfdir}/X11/
	ln -sf ${datadir}/fonts/X11 ${D}/${libdir}/X11/fonts

}

