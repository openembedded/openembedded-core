DESCRIPTION = "X Composite extension library."

require xorg-lib-common.inc

LICENSE= "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=90b90b60eb30f65fc9c2673d7cf59e24"

DEPENDS += " compositeproto virtual/libx11 libxfixes libxext"
PROVIDES = "xcomposite"

PE = "1"
PR = "r0"

XORG_PN = "libXcomposite"

SRC_URI += " file://change-include-order.patch"
