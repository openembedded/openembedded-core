include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"

PR = "r0"

DEPENDS += "libpthread-stubs xcb-proto-native"

PACKAGES =+ "libxcb-xinerama"
