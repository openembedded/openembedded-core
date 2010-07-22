require ../meta/packages/xorg-xserver/xserver-xf86-dri-lite.inc

PR = "r1"

PROTO_DEPS += "xf86driproto dri2proto"

DEPENDS += "font-util"

SRC_URI += "file://nodolt.patch;patch=1 \
            file://crosscompile.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1"

EXTRA_OECONF += "--enable-dri --enable-dri2 --enable-dga --enable-glx"

DEPENDS += "psb-firmware xpsb-glx psb-kernel-source libdrm-poulsbo libva"

COMPATBILE_MACHINE  = "emenlow"
