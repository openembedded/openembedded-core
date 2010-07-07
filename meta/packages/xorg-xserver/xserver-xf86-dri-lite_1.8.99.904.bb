require xserver-xf86-dri-lite.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3dd2bbe3563837f80ed8926b06c1c353"

PROTO_DEPS += "xf86driproto dri2proto"

DEPENDS += "font-util"

PE = "1"
PR = "r0"

SRC_URI += "file://nodolt.patch \
            file://crosscompile.patch"
#           file://libdri-xinerama-symbol.patch;patch=1 \
#           file://xserver-boottime.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch"

EXTRA_OECONF += "--enable-dri --enable-dri2"
