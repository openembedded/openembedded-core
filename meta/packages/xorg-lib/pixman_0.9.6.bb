require xorg-lib-common.inc

DESCRIPTION = "Library for lowlevel pixel operations"
DEPENDS = "virtual/libx11"

SRC_URI += "file://dont-copy-unused-bits-to-alpha-channel.patch;patch=1"
