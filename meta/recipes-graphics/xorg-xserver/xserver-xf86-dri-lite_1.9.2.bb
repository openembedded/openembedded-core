require xserver-xf86-dri-lite.inc


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

SRC_URI[md5sum] = "5c8773499a6a8c1ddaedf33577ec9634"
SRC_URI[sha256sum] = "8b30800004c98fc7a8e6ff31a339f28451be5132e774443be22bf226e1791e34"

EXTRA_OECONF += "--enable-dri --enable-dri2"
