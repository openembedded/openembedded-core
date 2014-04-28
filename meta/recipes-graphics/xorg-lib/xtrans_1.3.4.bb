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

SRC_URI[md5sum] = "a615e17d9fee6f097fc3b716eacb3dca"
SRC_URI[sha256sum] = "054d4ee3efd52508c753e9f7bc655ef185a29bd2850dd9e2fc2ccc33544f583a"
