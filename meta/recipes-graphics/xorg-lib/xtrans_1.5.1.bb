SUMMARY = "XTrans: X Transport library"

DESCRIPTION = "The X Transport Interface is intended to combine all \
system and transport specific code into a single place.  This API should \
be used by all libraries, clients and servers of the X Window System. \
Use of this API should allow the addition of new types of transports and \
support for new platforms without making any changes to the source \
except in the X Transport Interface code."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=bc875e1c864f4f62b29f7d8651f627fa"

SRC_URI += "file://multilibfix.patch"

PE = "1"

DEV_PKG_DEPENDENCY = ""

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[sha256sum] = "dea80fbd8c3c941495b4b1d2785cb652815d016849a0d2ef90d1140de916993e"
