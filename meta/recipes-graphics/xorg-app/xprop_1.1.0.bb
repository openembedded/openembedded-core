require xorg-app-common.inc

DESCRIPTION = "utility to display window and font properties of an X server"

LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94"

DEPENDS += " libxmu virtual/libx11"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "f0bacbd30f0dd1c1e9ccafe97687b7a4"
SRC_URI[sha256sum] = "cc8e07901574895f113baffda19272c54545879e02012314527ebbf2dcc66226"
