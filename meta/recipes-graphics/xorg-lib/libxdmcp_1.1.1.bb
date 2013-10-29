SUMMARY = "XDMCP: X Display Manager Control Protocol library"

DESCRIPTION = "The purpose of the X Display Manager Control Protocol \
(XDMCP) is to provide a uniform mechanism for an autonomous display to \
request login service from a remote host. An X terminal (screen, \
keyboard, mouse, processor, network interface) is a prime example of an \
autonomous display."

require xorg-lib-common.inc

inherit gettext

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d559fb26e129626022e052a5e6e0e123"

DEPENDS += "xproto"
PROVIDES = "xdmcp"

PE = "1"

XORG_PN = "libXdmcp"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "b94af6cef211cf3ee256f7e81f70fcd9"
SRC_URI[sha256sum] = "9ace6d4230f9dce4ed090692f82f613253ada8f887b23b3d8ff3dd4e3a7c118e"
