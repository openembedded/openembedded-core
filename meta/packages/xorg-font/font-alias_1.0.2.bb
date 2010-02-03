require xorg-font-common.inc

DESCRIPTION = "X font aliases."

DEPENDS = "virtual/xserver font-util"
RDEPENDS = "encodings font-util"

PE = "1"
PR = "${INC_PR}.0"
