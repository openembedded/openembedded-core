require xorg-lib-common.inc

DEPENDS += "xproto virtual/libx11"

SRC_URI += "file://fix-mtrr-check.patch;patch=1"
