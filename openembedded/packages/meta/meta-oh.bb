PACKAGES = "oh-base-depends oh-task-base"
# oh-task-base
DESCRIPTION = "Meta-package for OpenHand Palmtop Environment"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r2"

ALLOW_EMPTY = "1"

oh-base-depends = "\
    diet-x11 \
    virtual/xserver"

RDEPENDS_oh-base-depends := "${oh-base-depends}"
DEPENDS += " ${oh-base-depends}"

oh-task-base = "\
    matchbox \
    xcursor-transparent-theme \
    xserver-kdrive-common \
    avahi \
    rxvt \
    xhost \
    ttf-bitstream-vera \
    xauth"

#    teleport \
#    xst \
#    libgtkstylus \
#    detect-stylus \
#    xrdb \

RDEPENDS_oh-task-base := "${oh-task-base}"

#                          gdk-pixbuf-loader-png \
#			   gdk-pixbuf-loader-xpm \
#			   gdk-pixbuf-loader-jpeg \
#			   pango-module-basic-x \
#			   pango-module-basic-fc \

DEPENDS += " ${oh-task-base}"

LICENSE = "MIT"
