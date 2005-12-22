PACKAGES = "oh-base-depends oh-task-base"
# oh-task-base
DESCRIPTION = "Meta-package for OpenedHand Palmtop Environment"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r19"

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
    matchbox-config-gtk \
    matchbox-panel-manager \
    matchbox-panel-hacks \
    matchbox-themes-extra \
    matchbox-themes-gtk \		
    matchbox-applet-inputmanager \
    matchbox-applet-startup-monitor \
    xcursor-transparent-theme \
    xserver-kdrive-common \
    xserver-nodm-init \
    chkhinge26 \
    usbinit \
    settings-daemon \
    gtk+ \
    gtk-clearlooks-engine \
    eds-dbus \
    contacts \
    osso-addressbook \
    oprofile \
    dates \
    leafpad \
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

#    minimo \
#    teleport \
#    xst \
#    libgtkstylus \
#    detect-stylus \
#    xrdb \

RDEPENDS_oh-task-base := "${oh-task-base} \
                          avahi-daemon \
                          gdk-pixbuf-loader-png \
			  gdk-pixbuf-loader-xpm \
			  gdk-pixbuf-loader-jpeg \
			  gtk-theme-clearlooks \
                          kernel-module-oprofile \
                          tslib-calibrate \
                          tslib-tests \
			  pango-module-basic-x \
			  pango-module-basic-fc"

DEPENDS += " ${oh-task-base} avahi tslib"

LICENSE = "MIT"
