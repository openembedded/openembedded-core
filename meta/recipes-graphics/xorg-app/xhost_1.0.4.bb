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
PR = "r0"
PE = "1"

SRC_URI[md5sum] = "2be663a0afbcc0856c1591477d5bf32a"
SRC_URI[sha256sum] = "5e02c06caeb7a258f3621bf11459a7820cfeaf9bf269c1f8da86d7071346a594"
