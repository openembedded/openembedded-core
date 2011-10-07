require xserver-xorg.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=74df27b6254cc88d2799b5f4f5949c00"

PROTO_DEPS += "xf86driproto dri2proto"

DEPENDS += "font-util"

PE = "1"
PR = "r3"

SRC_URI += "file://crosscompile.patch"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch"

SRC_URI[md5sum] = "75f117c74f2ecaf9dd167f6a66ac98de"
SRC_URI[sha256sum] = "143c7c3d7d4428352e1153dffa34fd64af391f72d30b2a03e911e54e36f00b5d"

EXTRA_OECONF += "--enable-dri --enable-dri2"
