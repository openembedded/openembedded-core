require xserver-xf86-dri-lite.inc

PE = "1"
PR = "r1"
PV = "1.5.99.1+git${SRCREV}"

PROTO_DEPS += "xf86driproto"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/xserver;protocol=git \
           file://xorg.conf \
           file://libdri-xinerama-symbol.patch;patch=1 \
           file://xserver-boottime.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1"

EXTRA_OECONF += "--enable-dri --disable-dri2"

S = "${WORKDIR}/git"
