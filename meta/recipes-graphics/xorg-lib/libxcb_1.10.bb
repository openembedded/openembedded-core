include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"


DEPENDS += "libxdmcp"

SRC_URI += "file://ensure-xcb-owns-socket-and-no-other-threads-are-writ.patch \
"

SRC_URI[md5sum] = "074c335cc4453467eeb234e3dadda700"
SRC_URI[sha256sum] = "98d9ab05b636dd088603b64229dd1ab2d2cc02ab807892e107d674f9c3f2d5b5"
