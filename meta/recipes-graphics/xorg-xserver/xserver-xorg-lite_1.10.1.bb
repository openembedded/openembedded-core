require xserver-xorg-lite.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=74df27b6254cc88d2799b5f4f5949c00"

PR = "r3"

DEPENDS += "font-util"

SRC_URI += "file://crosscompile.patch"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;"
