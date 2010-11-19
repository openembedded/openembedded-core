SUMMARY = "X transport library"

DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=49347921d4d5268021a999f250edc9ca"

PE = "1"
PR = "r0"

RDEPENDS_${PN}-dev = ""
DEPENDS += "gettext"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "c66f9ffd2da4fb012220c6c40ebc7609"
SRC_URI[sha256sum] = "c5f9a73705ddbb8c9b8f16c4fac33b4b9ba7661b8305474b4c1549e48d9ca5c6"
