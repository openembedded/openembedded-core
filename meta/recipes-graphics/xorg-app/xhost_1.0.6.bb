require xorg-app-common.inc

SUMMARY = "Server access control program for X"

DESCRIPTION = "The xhost program is used to add and delete host names or \
user names to the list allowed to make connections to the X server. In \
the case of hosts, this provides a rudimentary form of privacy control \
and security. Environments which require more sophisticated measures \
should implement the user-based mechanism or use the hooks in the \
protocol for passing other authentication data to the server."

LIC_FILES_CHKSUM = "file://COPYING;md5=8fbed71dddf48541818cef8079124199"
DEPENDS += "libxmu libxau"
PE = "1"

SRC_URI[md5sum] = "f1669af1fe0554e876f03319c678e79d"
SRC_URI[sha256sum] = "a6f5b922df0a7be5d3ba43f525fa8e69c539c738418f013a0b7adaa423a89dc2"
