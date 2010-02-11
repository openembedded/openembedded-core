require xserver-kdrive.inc

DEPENDS += "libxkbfile libxcalibrate font-util"

RDEPENDS_${PN} += "xkeyboard-config"

EXTRA_OECONF += "--disable-glx"

PE = "1"
PR = "r23"

SRC_URI = "${XORG_MIRROR}/individual/xserver/xorg-server-${PV}.tar.bz2 \
	file://extra-kmodes.patch;patch=1 \
	file://disable-apm.patch;patch=1 \
	file://no-serial-probing.patch;patch=1 \
#	file://kdrive-evdev.patch;patch=1  \
#	file://kdrive-use-evdev.patch;patch=1  \
#	file://enable-builtin-fonts.patch;patch=1 \
#	file://disable-xf86-dga-xorgcfg.patch;patch=1 \
#	file://optional-xkb.patch;patch=1 \
	file://enable-xcalibrate.patch;patch=1 \
	file://hide-cursor-and-ppm-root.patch;patch=1 \
	file://fbdev_xrandr_ioctl.patch;patch=1 \
	file://fix-newer-xorg-headers.patch;patch=1 \
        file://crosscompile.patch;patch=1"

S = "${WORKDIR}/xorg-server-${PV}"
