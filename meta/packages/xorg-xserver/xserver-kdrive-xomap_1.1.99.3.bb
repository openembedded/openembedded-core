LICENSE = "MIT"
DEPENDS = "tslib xproto libxdmcp xextproto xtrans libxau virtual/libx11 libxext libxrandr fixesproto damageproto libxfont resourceproto compositeproto calibrateproto recordproto videoproto scrnsaverproto xpext xsp libxkbfile dbus"

PROVIDES = "virtual/xserver"
PACKAGES =+ "xserver-kdrive-xomap"
SECTION = "x11/base"
DESCRIPTION = "X server from freedesktop.org"
DESCRIPTION_xserver-kdrive-xomap = "X server for the OMAP in the Nokia 800"

PE = "1"

COMPATIBLE_MACHINE = "nokia(800|770)"

FILES_${PN} = "${libdir}/xserver /etc/dbus-1/* ${bindir}/Xomap"

SRC_URI = "http://repository.maemo.org/pool/maemo3.1/free/source/xorg-server_1.1.99.3-0osso31.tar.gz \
	file://kmode.patch;patch=1 \
	file://disable-apm.patch;patch=1 \
	file://no-serial-probing.patch;patch=1 \
	file://fbdev-not-fix.patch;patch=1  \
	file://enable-builtin-fonts.patch;patch=1 \
	file://xcalibrate.patch;patch=1 \
	file://fixups.patch;patch=1 \
	file://button_only.patch;patch=1 \
	file://calibrateext.patch;patch=1 \
	file://xcalibrate_coords.patch;patch=1"
#	file://kdrive-evdev.patch;patch=1  \
#	file://kdrive-use-evdev.patch;patch=1  \
#	file://optional-xkb.patch;patch=1 \
#	file://disable-xf86-dga-xorgcfg.patch;patch=1 \
#	file://enable-tslib.patch;patch=1 \
#	file://xfbdev-fb-opt.patch;patch=1"

S = "${WORKDIR}/xorg-server-1.1.99.3"

inherit autotools pkgconfig 

EXTRA_OECONF = "--enable-composite --enable-kdrive --enable-builtin-fonts \
		--disable-dga --disable-dri --disable-xinerama \
		--disable-xf86misc --disable-xf86vidmode \
		--disable-xorg --disable-xorgcfg \
		--disable-dmx --enable-xcalibrate \ 
		--disable-xkb --disable-xnest --disable-xvfb \
		--disable-xevie --disable-xprint --disable-xtrap \
		--with-default-font-path=built-ins \
		ac_cv_file__usr_share_X11_sgml_defs_ent=no \
		--enable-xomap"

do_configure_prepend() {
    sed -i -e 's/tslib-0.0/tslib-1.0/' ${S}/configure.ac
}
