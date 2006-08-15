require xorg-lib-common.inc

DESCRIPTION = "X Cursor library"
LICENSE= "BSD-X"

DEPENDS += " libxrender libxfixes virtual/libx11 fixesproto"

XORG_PN = "libXcursor"

FILES_${PN} += "${libdir}/libXcursor.so"
