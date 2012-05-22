SUMMARY = "XTrans: X Transport library"

DESCRIPTION = "The X Transport Interface is intended to combine all \
system and transport specific code into a single place.  This API should \
be used by all libraries, clients and servers of the X Window System. \
Use of this API should allow the addition of new types of transports and \
support for new platforms without making any changes to the source \
except in the X Transport Interface code."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=49347921d4d5268021a999f250edc9ca"

PE = "1"
PR = "r0"

RDEPENDS_${PN}-dev = ""

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "84c66908cf003ad8c272b0eecbdbaee3"
SRC_URI[sha256sum] = "7f811191ba70a34a9994d165ea11a239e52c527f039b6e7f5011588f075fe1a6"
