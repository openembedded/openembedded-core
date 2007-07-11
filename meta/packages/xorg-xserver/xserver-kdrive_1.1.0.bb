LICENSE = "MIT"
DEPENDS = "tslib xproto libxdmcp xextproto xtrans libxau virtual/libx11 libxext libxrandr fixesproto damageproto libxfont resourceproto compositeproto calibrateproto recordproto videoproto scrnsaverproto"

PROVIDES = "virtual/xserver"
# RPROVIDES = "virtual/xserver"
PACKAGES =+ "xserver-kdrive-fbdev xserver-kdrive-fake xserver-kdrive-xephyr"
SECTION = "x11/base"
DESCRIPTION = "X server from freedesktop.org"
DESCRIPTION_xserver-kdrive-fbdev = "X server from freedesktop.org, supporting generic framebuffer devices"
DESCRIPTION_xserver-kdrive-fake = "Fake X server"
DESCRIPTION_xserver-kdrive-xephyr = "X server in an X window"

PE = "1"

FILES_${PN} = "${libdir}/xserver"
FILES_xserver-kdrive-fbdev = "${bindir}/Xfbdev"
FILES_xserver-kdrive-fake = "${bindir}/Xfake"
FILES_xserver-kdrive-xephyr = "${bindir}/Xephyr"

RDEPENDS_xserver-kdrive-fbdev = "${PN}"
RDEPENDS_xserver-kdrive-fake = "${PN}"
RDEPENDS_xserver-kdrive-xephyr = "${PN}"

SRC_URI = "${XORG_MIRROR}/individual/xserver/xorg-server-1.1.0.tar.bz2 \
	file://kmode.patch;patch=1 \
	file://disable-apm.patch;patch=1 \
	file://no-serial-probing.patch;patch=1 \
	file://kdrive-evdev.patch;patch=1  \
	file://kdrive-use-evdev.patch;patch=1  \
	file://fbdev-not-fix.patch;patch=1  \
	file://enable-builtin-fonts.patch;patch=1 \
	file://optional-xkb.patch;patch=1 \
	file://disable-xf86-dga-xorgcfg.patch;patch=1 \
	file://enable-tslib.patch;patch=1 \
	file://xcalibrate.patch;patch=1 \
	file://xfbdev-fb-opt.patch;patch=1 \
	file://hide-cursor-and-ppm-root.patch;patch=1"

SRC_URI_append_mnci   = " file://onlyfb.patch;patch=1"
SRC_URI_append_poodle = " file://xserver-kdrive-poodle.patch;patch=1"
SRC_URI_append_qemux86 = " file://xserver-kdrive-poodle.patch;patch=1"
PACKAGE_ARCH_poodle = "poodle"

S = "${WORKDIR}/xorg-server-1.1.0"

inherit autotools pkgconfig 

EXTRA_OECONF = "--enable-composite --enable-kdrive \
		--disable-dga --disable-dri --disable-xinerama \
		--disable-xf86misc --disable-xf86vidmode \
		--disable-xorg --disable-xorgcfg \
		--disable-dmx \ 
		--disable-xkb --disable-xnest --disable-xvfb \
		--disable-xevie --disable-xprint --disable-xtrap \
		--with-default-font-path=built-ins \
		ac_cv_file__usr_share_X11_sgml_defs_ent=no"

do_configure_prepend() {
    sed -i -e 's/tslib-0.0/tslib-1.0/' ${S}/configure.ac
}
