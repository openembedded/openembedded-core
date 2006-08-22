require xorg-app-common.inc

#DESCRIPTION = ""

DEPENDS += " virtual/libx11"
FILES_${PN} += "  /usr/lib/X11/xinit"

