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

SRC_URI[md5sum] = "2f14c31ab556fc91039f51a113b38aa2"
SRC_URI[sha256sum] = "622db4adce224581a44fbe41321bbb0bdc0c78aec586ba83548f1f1c6e8a09bf"
