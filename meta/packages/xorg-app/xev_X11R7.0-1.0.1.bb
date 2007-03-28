require xorg-app-common.inc

DESCRIPTION = "X Event Viewer"
LICENSE = "MIT"

DEPENDS += " virtual/libx11"

SRC_URI += "file://diet-x11.patch;patch=1"
