PV = "0.0+cvs${FIXEDSRCDATE}"
FIXEDSRCDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
DEFAULT_PREFERENCE = "1"

LICENSE = "MIT"
DEPENDS = "tslib xproto libxdmcp xextproto xtrans libxau libx11 libxext libxrandr fixesproto damageproto libxfont resourceproto compositeproto xcalibrateext recordproto videoproto scrnsaverproto"
PROVIDES = "virtual/xserver"
RPROVIDES = "virtual/xserver"
PACKAGES = "xserver-kdrive-mach64 xserver-kdrive-fbdev xserver-kdrive-vesa xserver-kdrive-mga xserver-kdrive-via xserver-kdrive-ati xserver-kdrive-fake xserver-kdrive-i810 xserver-kdrive-xephyr xserver-kdrive-epson ${PN}-doc ${PN}-dev ${PN}-locale"
SECTION = "x11/base"
DESCRIPTION = "X server from freedesktop.org"
DESCRIPTION_xserver-kdrive-i810 = "X server from freedesktop.org, supporting i810 devices"
DESCRIPTION_xserver-kdrive-ati = "X server from freedesktop.org, supporting ATI devices"
DESCRIPTION_xserver-kdrive-mga = "X server from freedesktop.org, supporting MGA devices"
DESCRIPTION_xserver-kdrive-vesa = "X server from freedesktop.org, supporting generic VESA devices"
DESCRIPTION_xserver-kdrive-mach64 = "X server from freedesktop.org, supporting Mach64 devices"
DESCRIPTION_xserver-kdrive-via = "X server from freedesktop.org, supporting VIA CLE266 devices"
DESCRIPTION_xserver-kdrive-fbdev = "X server from freedesktop.org, supporting generic framebuffer devices"
DESCRIPTION_xserver-kdrive-epson = "X server from freedesktop.org, supporting Epson S1D13806 devices"
DESCRIPTION_xserver-kdrive-fake = "Fake X server"
DESCRIPTION_xserver-kdrive-xephyr = "X server in an X window"

PR = "r11"

FILES_xserver-kdrive-fbdev = "${bindir}/Xfbdev"
FILES_xserver-kdrive-ati = "${bindir}/Xati"
FILES_xserver-kdrive-vesa = "${bindir}/Xvesa"
FILES_xserver-kdrive-via = "${bindir}/Xvia"
FILES_xserver-kdrive-mga = "${bindir}/Xmga"
FILES_xserver-kdrive-mach64 = "${bindir}/Xmach64"
FILES_xserver-kdrive-fake = "${bindir}/Xfake"
FILES_xserver-kdrive-i810 = "${bindir}/Xi810"
FILES_xserver-kdrive-epson = "${bindir}/Xepson"
FILES_xserver-kdrive-xephyr = "${bindir}/Xephyr"

SRC_URI = "${FREEDESKTOP_CVS}/xserver;module=xserver \
	file://kmode.patch;patch=1 \
	file://disable-apm.patch;patch=1 \
	file://no-serial-probing.patch;patch=1 \
	file://kdrive-evdev.patch;patch=1  \
	file://kdrive-use-evdev.patch;patch=1  \
	file://fbdev-not-fix.patch;patch=1"

SRC_URI_append_mnci   = " file://onlyfb.patch;patch=1"
SRC_URI_append_poodle = " file://xserver-kdrive-poodle.patch;patch=1"
PACKAGE_ARCH_poodle = "poodle"

S = "${WORKDIR}/xserver"

inherit autotools pkgconfig 

EXTRA_OECONF = "--enable-composite --disable-xinerama"
