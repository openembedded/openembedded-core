DESCRIPTION = "X Composite extension library."

require xorg-lib-common.inc

LICENSE= "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=3f2907aad541f6f226fbc58cc1b3cdf1"

DEPENDS += " compositeproto virtual/libx11 libxfixes libxext"
PROVIDES = "xcomposite"

PE = "1"
PR = "r0"

XORG_PN = "libXcomposite"

SRC_URI += " file://change-include-order.patch"
