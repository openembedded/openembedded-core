require xserver-kdrive.inc

DEPENDS += "libxkbfile libxcalibrate"

PE = "1"
PR = "r16"

SRC_URI = "${XORG_MIRROR}/individual/xserver/xorg-server-${PV}.tar.bz2 \
	file://kmode.patch;patch=1 \
	file://disable-apm.patch;patch=1 \
	file://no-serial-probing.patch;patch=1 \
	file://kdrive-evdev.patch;patch=1  \
	file://kdrive-use-evdev.patch;patch=1  \
	file://fbdev-not-fix.patch;patch=1  \
	file://enable-builtin-fonts.patch;patch=1 \
	file://disable-xf86-dga-xorgcfg.patch;patch=1 \
	file://optional-xkb.patch;patch=1 \
	file://enable-epson.patch;patch=1 \
	file://enable-tslib.patch;patch=1 \
	file://kmode-palm.patch;patch=1 \
	file://fix_default_mode.patch;patch=1 \
	file://enable-xcalibrate.patch;patch=1 \
	file://hide-cursor-and-ppm-root.patch;patch=1 \
	file://xcalibrate_coords.patch;patch=1"

S = "${WORKDIR}/xorg-server-${PV}"
