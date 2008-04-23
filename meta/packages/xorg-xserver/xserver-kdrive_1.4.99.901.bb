require xserver-kdrive.inc

DEPENDS += "hal libxkbfile libxcalibrate pixman"

DEFAULT_PREFERENCE = "-99" 

PE = "1"
PR = "r3"

SRC_URI = "${XORG_MIRROR}/individual/xserver/xorg-server-${PV}.tar.bz2 \
	file://extra-kmodes.patch;patch=1 \
	file://disable-apm.patch;patch=1 \
	file://no-serial-probing.patch;patch=1 \
	file://fbdev-not-fix.patch;patch=1  \
	file://optional-xkb.patch;patch=1 \
	file://enable-tslib.patch;patch=1;status=merged \
	file://enable-epson.patch;patch=1;status=merged \
#	file://hide-cursor-and-ppm-root.patch;patch=1 \
#	file://xcalibrate_coords.patch;patch=1 \
	file://w100.patch;patch=1 \
	file://w100-autofoo.patch;patch=1 \
	file://w100-fix-offscreen-bmp.patch;patch=1 \
	file://w100-new-input-world-order.patch;patch=1 \
	file://linux-keyboard-mediumraw.patch;patch=1;status=merged \
	file://xcalibrate-new-input-world-order.patch;patch=1 \
	file://tslib-default-device.patch;patch=1;status=merged \
#	file://fbdev-evdev.patch;patch=1 \
	file://keyboard-resume-workaround.patch;patch=1 \
	file://xorg-avr32-support.diff;patch=1;status=merged \
#	file://pkgconfig_fix.patch;patch=1 \
	file://no_xkb.patch;patch=1;pnum=0;status=merged \
        "

S = "${WORKDIR}/xorg-server-${PV}"

W100_OECONF = "--disable-w100"
#W100_OECONF_arm = "--enable-w100"

EXTRA_OECONF += "--enable-builtin-fonts \
		 --disable-dri2 \
		"
