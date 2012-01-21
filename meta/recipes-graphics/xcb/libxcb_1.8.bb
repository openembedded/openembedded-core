include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"

PR = "r0"

DEPENDS += "libpthread-stubs xcb-proto-native libxdmcp"

PACKAGES =+ "libxcb-xinerama"

SRC_URI[md5sum] = "eba59a8887ba6dbaa4cf337766f8d19d"
SRC_URI[sha256sum] = "25c3600bec104c5aa6e9f559bfe0011b0e223bde726b849b58f758c2d4e6fc5e"
