include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"

PR = "r0"

DEPENDS += "libpthread-stubs xcb-proto-native libxdmcp"

PACKAGES =+ "libxcb-xinerama"

SRC_URI[md5sum] = "9da03df9e2f4c048202920d9f6a7e123"
SRC_URI[sha256sum] = "d2f46811e950710e7e79e45615d24f2c7ec318b9de9dc717972723da58bffa0d"
