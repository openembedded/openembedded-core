DESCRIPTION = "xterm app"
SECTION = "x11/applications"
LICENSE = "MIT-X"

DEPENDS = "libxaw xproto virtual/libx11 xextproto xext xau libxpm ncurses"

SRC_URI = "${XORG_MIRROR}/development/X11R7.0-RC4/extras/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig

FILES_${PN} += " /usr/lib/X11"

#EXTRA_OERECONF = " -I${S}/xterm.m4"
EXTRA_OECONF = " --x-includes=${STAGING_INCDIR} --x-libraries=${STAGING_LIBDIR} FREETYPE_CONFIG=${STAGING_BINDIR_CROSS}/freetype-config --disable-imake"

#do_configure_prepend () {
#	mv ${S}/aclocal.m4 ${S}/xterm.m4
#}
do_configure() {

	sed -e "s%/usr/contrib/X11R6%${STAGING_LIBDIR}%g" -i configure

	oe_runconf
}

do_stage() {
	autotools_stage_all
}
