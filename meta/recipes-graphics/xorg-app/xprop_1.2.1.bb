require xorg-app-common.inc

SUMMARY = "Utility to display window and font properties of an X server"

DESCRIPTION = "The xprop utility is for displaying window and font \
properties in an X server. One window or font is selected using the \
command line arguments or possibly in the case of a window, by clicking \
on the desired window. A list of properties is then given, possibly with \
formatting information."

LIC_FILES_CHKSUM = "file://COPYING;md5=e226ab8db88ac0bc0391673be40c9f91"

DEPENDS += " libxmu virtual/libx11"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "d5529dc8d811efabd136ca2d8e857deb"
SRC_URI[sha256sum] = "07907a189099b8a000406b5ca6c5346df238a9e1fe3b32dc59f48fe0ad12f1a3"
