require xserver-xf86-dri-lite.inc

PE = "1"
PR = "r4"

PROTO_DEPS += "xf86driproto"

SRC_URI += "file://drmfix.patch;patch=1 \
            file://libdri-xinerama-symbol.patch;patch=1 "

EXTRA_OECONF += "--enable-dri"

