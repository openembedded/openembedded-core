require xf86-input-common.inc

DESCRIPTION = "X.Org X server -- VMWare mouse input driver"
PR = "r0"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=622841c068a9d7625fbfe7acffb1a8fc"
