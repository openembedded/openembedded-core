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

RDEPENDS_${PN}-dev = ""

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "1942deb831fd21703e30ebe47794d60c"
SRC_URI[sha256sum] = "3ae7600e8553ccf2f5c46fbcc63d887729637a4b2a898927dcd339a13355bd4a"
