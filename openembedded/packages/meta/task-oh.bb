PACKAGES = "task-oh-base-depends task-oh-base"
DESCRIPTION = "Tasks for OpenedHand Poky"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r22"

ALLOW_EMPTY = "1"

RDEPENDS_task-oh-base-depends := "\
    diet-x11 \
    virtual/xserver"

RDEPENDS_task-oh-base := "\
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
    dates \
    web \
    libcurl \
    js \
    pcmanfm \
    leafpad \
    puzzles \
    kf \
    rxvt-unicode \
    xhost \
#    oprofile \
#    gdb \    
    strace \
    ttf-bitstream-vera \
    udev \
    sysfsutils \
    xauth \
    avahi-daemon \
    gdk-pixbuf-loader-png \
    gdk-pixbuf-loader-gif \
    gdk-pixbuf-loader-xpm \
    gdk-pixbuf-loader-jpeg \
    gnome-vfs-plugin-dbus \
    gnome-vfs-plugin-file \
    gnome-vfs-plugin-http \
    gtk-theme-clearlooks \
    kernel-module-oprofile \
    tslib-calibrate \
    tslib-tests \
    pango-module-basic-x \
    pango-module-basic-fc"


#    minimo \
#    teleport \
#    xst \
#    libgtkstylus \
#    detect-stylus \
#    xrdb \

LICENSE = "MIT"
