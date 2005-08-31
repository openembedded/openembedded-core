PACKAGES = oh-base-depends 
# oh-task-base
DESCRIPTION = "Meta-package for OpenHand Palmtop Environment"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r0"

ALLOW_EMPTY = "1"

oh-base-depends = "\
    diet-x11 \
    matchbox \
    virtual/xserver"

RDEPENDS_oh-base-depends := "${oh-base-depends}"
DEPENDS += " ${oh-base-depends}"

#oh-task-base = "\
#    matchbox \
#    xcursor-transparent-theme \
#    rxvt-unicode \
#    xst \
#    xhost \
#    xrdb \
#    ttf-bitstream-vera \
#    libgtkstylus \
#    detect-stylus \
#    teleport \
#    xauth"
#
#RDEPENDS_gpe-task-base := "gdk-pixbuf-loader-png \
#			   gdk-pixbuf-loader-xpm \
#			   gdk-pixbuf-loader-jpeg \
#			   pango-module-basic-x \
#			   pango-module-basic-fc \
#			   ${gpe-task-base}"
#DEPENDS += " ${gpe-task-base}"

LICENSE = "MIT"
