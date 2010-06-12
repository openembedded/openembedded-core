require xf86-input-common.inc

DESCRIPTION = "X.Org X server -- VMWare mouse input driver"
PR = "r1"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=d1f16420e5ed7ed2133768425dfdab50"
