require xserver-xf86-dri-lite.inc

PE = "1"
PR = "r0"
PV = "1.5.99.1+git${SRCREV}"

PROTO_DEPS += "dri2proto"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/xserver;protocol=git \
           file://xorg.conf \
           file://libdri-xinerama-symbol.patch;patch=1"

# Copied from OBS
SRC_URI += "file://xorg-server-enable-dri2.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1"

EXTRA_OECONF += "--enable-dri2"

S = "${WORKDIR}/git"
