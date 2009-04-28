require xserver-xf86-dri-lite.inc

PE = "1"
PR = "r0"

PROTO_DEPS += "xf86driproto"

SRC_URI += "file://nodolt.patch;patch=1 \
           file://libdri-xinerama-symbol.patch;patch=1 \
           file://xserver-boottime.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1"

EXTRA_OECONF += "--enable-dri --enable-dri2"
