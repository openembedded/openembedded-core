require xorg-app-common.inc
PE = "1"

DESCRIPTION = "tiny window manager"

DEPENDS += " virtual/libx11 libxext libxt libxmu"

ALTERNATIVE_PATH = "${bindir}/twm"
ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PRIORITY = "1"
