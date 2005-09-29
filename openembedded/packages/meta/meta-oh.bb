PACKAGES = "oh-base-depends oh-task-base"
# oh-task-base
DESCRIPTION = "Meta-package for OpenHand Palmtop Environment"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r10"

ALLOW_EMPTY = "1"

oh-base-depends = "\
    diet-x11 \
    virtual/xserver"

RDEPENDS_oh-base-depends := "${oh-base-depends}"
DEPENDS += " ${oh-base-depends}"

oh-task-base = "\
    matchbox \
    matchbox-poky \
    matchbox-keyboard \
    matchbox-stroke \
    matchbox-panel-manager \
    matchbox-panel-hacks \
    matchbox-themes-extra \
    matchbox-themes-gtk \		
    matchbox-applet-inputmanager \
    matchbox-applet-startup-monitor \
    xcursor-transparent-theme \
    xserver-kdrive-common \
    xserver-nodm-init \
    usbinit \
    gtk+ \
    gtk-clearlooks-engine \
    eds-dbus \
    puzzles \
    kf \
    rxvt-unicode \
    xhost \
    gdb \    
    strace \
    ttf-bitstream-vera \
    udev \
    sysfsutils \
    xauth"

#    avahi-daemon \
#    avahi-dnsconfd \
#    teleport \
#    xst \
#    libgtkstylus \
#    detect-stylus \
#    xrdb \

RDEPENDS_oh-task-base := "${oh-task-base} \
                          gdk-pixbuf-loader-png \
			  gdk-pixbuf-loader-xpm \
			  gdk-pixbuf-loader-jpeg \
                          tslib-calibrate \
                          tslib-tests \
                          puzzles-desktop \
			  pango-module-basic-x \
			  pango-module-basic-fc"

DEPENDS += " ${oh-task-base} tslib"

LICENSE = "MIT"
