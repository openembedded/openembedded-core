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

SRC_URI[md5sum] = "5bef6839a76d029204ab31aa2fcb5201"
SRC_URI[sha256sum] = "864831f51e841ff37f2445d1c85b86b559c8860a435fb496aead4f256a2b141d"

EXTRA_OECONF += "--enable-dri --enable-dri2"
