require xserver-kdrive.inc

DEPENDS += "libxkbfile libxcalibrate font-util pixman"

RDEPENDS_${PN} += "xkeyboard-config"

EXTRA_OECONF += "--disable-glx"

PE = "1"
PR = "r26"

SRC_URI = "${XORG_MIRROR}/individual/xserver/xorg-server-${PV}.tar.bz2 \
	file://extra-kmodes.patch \
	file://disable-apm.patch \
	file://no-serial-probing.patch \
	file://keyboard-resume-workaround.patch \
	file://enable-xcalibrate.patch \
	file://hide-cursor-and-ppm-root.patch \
	file://fbdev_xrandr_ioctl.patch \
	file://fix-newer-xorg-headers.patch \
	file://crosscompile.patch \
	file://nodolt.patch"
#	file://kdrive-evdev.patch
#	file://kdrive-use-evdev.patch
#	file://enable-builtin-fonts.patch
#	file://optional-xkb.patch


S = "${WORKDIR}/xorg-server-${PV}"
