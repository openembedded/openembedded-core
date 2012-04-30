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
PR = "${INC_PR}.0"
PE = "1"

SRC_URI[md5sum] = "a0fcd2cb6ddd9f378944cc6f4f83cd7c"
SRC_URI[sha256sum] = "2870d19f3f4867ead5ba4e35bb73d1fa302be29d812c13e4195066c78d1f8850"
