PACKAGES = "oh-base-depends oh-task-base"
# oh-task-base
DESCRIPTION = "Meta-package for OpenedHand Palmtop Environment"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r18"

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
    minimo \
    gtk+ \
    gtk-clearlooks-engine \
    eds-dbus \
    contacts \
    osso-addressbook \
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

#    avahi-daemon \
#    avahi-dnsconfd \
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
                          tslib-calibrate \
                          tslib-tests \
			  pango-module-basic-x \
			  pango-module-basic-fc"

DEPENDS += " ${oh-task-base} avahi tslib"

LICENSE = "MIT"
