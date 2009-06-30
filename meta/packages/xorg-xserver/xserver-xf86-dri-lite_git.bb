require xserver-xf86-dri-lite.inc

PE = "1"
PR = "r5"
PV = "1.6.0+git${SRCREV}"

PROTO_DEPS += "xf86driproto dri2proto"

RDEPENDS_${PN} += "xkeyboard-config"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/xserver;protocol=git;branch=server-1.6-branch \
	   file://nodolt.patch;patch=1 \
           file://libdri-xinerama-symbol.patch;patch=1 \
           file://xserver-boottime.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1"

EXTRA_OECONF += "--enable-dri --enable-dri2 --enable-dga"

S = "${WORKDIR}/git"
