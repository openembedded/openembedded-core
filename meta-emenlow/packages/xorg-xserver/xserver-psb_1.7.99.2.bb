require recipes-graphics/xorg-xserver/xserver-xf86-dri-lite.inc

PR = "r3"

PROTO_DEPS += "xf86driproto dri2proto"

DEPENDS += "font-util"

SRC_URI += "file://nodolt.patch \
            file://crosscompile.patch \
	    file://libdrm-poulsbo.patch"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch"

EXTRA_OECONF += "--enable-dri --enable-dri2 --enable-dga --enable-glx"

RDEPENDS_${PN} += "xserver-xorg-video-psb psb-firmware xpsb-glx \
	       libdrm-poulsbo libva"

COMPATIBLE_MACHINE = "emenlow"
