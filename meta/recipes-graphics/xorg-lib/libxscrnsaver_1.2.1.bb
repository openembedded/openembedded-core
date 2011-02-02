require xorg-lib-common.inc

SUMMARY = "Xscrnsaver: X Screen Saver extension library"

DESCRIPTION = "The X Window System provides support for changing the \
image on a display screen after a user-settable period of inactivity to \
avoid burning the cathode ray tube phosphors. However, no interfaces are \
provided for the user to control the image that is drawn. This extension \
allows an external \"screen saver\" client to detect when the alternate \
image is to be displayed and to provide the graphics."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=21fd154ee757813632ada871a34113fb"

DEPENDS += "libxext scrnsaverproto"
PROVIDES = "libxss"
RREPLACES = "libxss"
PR = "r0"
PE = "1"

XORG_PN = "libXScrnSaver"

SRC_URI[md5sum] = "898794bf6812fc9be9bf1bb7aa4d2b08"
SRC_URI[sha256sum] = "ce3a66e2f6fa85b22280ab9cc1b2a113a2cb0ade9470914c7c3d6ac1d44b259a"
