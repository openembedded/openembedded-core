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

SRC_URI[md5sum] = "e6d0673a1e1e469f0a6220a6868fb94b"
SRC_URI[sha256sum] = "53508c082b03dbbfd68489cb21392023f3e62ca9c921e1fb39b92ad10fbe6b9c"
