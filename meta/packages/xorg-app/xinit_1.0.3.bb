require xorg-app-common.inc
PE = "1"

#DESCRIPTION = ""

DEPENDS += " virtual/libx11"
FILES_${PN} += "  /usr/lib/X11/xinit"

