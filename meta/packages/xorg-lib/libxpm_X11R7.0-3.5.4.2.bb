require xorg-lib-common.inc

DESCRIPTION = "X Pixmap library."
PRIORITY = "optional"
LICENSE = "X-BSD"

DEPENDS += " xproto virtual/libx11 libxt libxext xextproto"

XORG_PN = "libXpm"

PACKAGES =+ "sxpm cxpm"
FILES_cxpm = "${bindir}/cxpm"
FILES_sxpm = "${bindir}/sxpm"
