SUMMARY = "ICE: Inter-Client Exchange library"

DESCRIPTION = "The Inter-Client Exchange (ICE) protocol provides a \
generic framework for building protocols on top of reliable, byte-stream \
transport connections. It provides basic mechanisms for setting up and \
shutting down connections, for performing authentication, for \
negotiating versions, and for reporting errors. "

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d162b1b3c6fa812da9d804dcf8584a93"

DEPENDS += "xproto xtrans"
PROVIDES = "ice"

PR = "r0"
PE = "1"

XORG_PN = "libICE"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "bb72a732b15e9dc25c3036559387eed5"
SRC_URI[sha256sum] = "a8b1692f151a473cee8733df9aefe98f7e5f64dfe6d4213cb6231d7bf855b901"
