#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Tasks for OpenedHand Poky"
PR = "r2"

PACKAGES = "\
    task-poky-base \
    task-poky-base-dbg \
    task-poky-base-dev \
    task-poky-boot \
    task-poky-boot-dbg \
    task-poky-boot-dev \
    task-poky-standard \
    task-poky-standard-dbg \
    task-poky-standard-dev \
    task-poky-boot-extras \
    task-poky-boot-extras-dbg \
    task-poky-boot-extras-dev \
    task-poky-devtools \
    task-poky-devtools-dbg \
    task-poky-devtools-dev \
    task-poky-testapps \
    task-poky-testapps-dbg \
    task-poky-testapps-dev \
    task-poky-nfs-server \
    task-poky-nfs-server-dbg \
    task-poky-nfs-server-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

XSERVER ?= "xserver-kdrive-fbdev"

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-boot-extras = "\
    task-base"

RDEPENDS_task-poky-base = "\
    psplash \
    matchbox-common \
    matchbox-wm \
    matchbox-keyboard \
    matchbox-keyboard-applet \
    matchbox-keyboard-im \
    matchbox-panel-2 \
    matchbox-desktop \
    ${XSERVER} \
    xserver-kdrive-common \
    xserver-nodm-init \
    liberation-fonts \
    xauth \
    xhost \
    xset \
    xrandr \
    udev \
    sysfsutils \
    gdk-pixbuf-loader-png \
    gdk-pixbuf-loader-gif \
    gdk-pixbuf-loader-xpm \
    gdk-pixbuf-loader-jpeg \
    pango-module-basic-x \
    pango-module-basic-fc \
    gtk+ "

RDEPENDS_task-poky-standard = "\
    leafpad \
    dropbear \
    portmap \
    matchbox-desktop \
    matchbox-sato \
    matchbox-keyboard \
    matchbox-stroke \
    matchbox-config-gtk \
    matchbox-themes-gtk \		
    matchbox-applet-startup-monitor \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    eds-dbus \
    contacts \
    dates \
    tasks \
    web \
    pcmanfm \
    puzzles \
    rxvt-unicode \
    screenshot \
    avahi-daemon \
    gnome-vfs \
    gnome-vfs-plugin-file \
    gnome-vfs-plugin-http"
#    matchbox-applet-inputmanager 

RDEPENDS_task-poky-devtools = "\
    oprofile \
    oprofileui-server \
    gdb \    
    strace \
    less \
    lttng-viewer"
RRECOMMENDS_task-poky-devtools = "\
    kernel-module-oprofile"

RDEPENDS_task-poky-testapps = "\
    tslib-calibrate \
    tslib-tests \
    lrzsz \
    alsa-utils-amixer \
    alsa-utils-aplay \
    owl-video-widget \
    gst-meta-video \
    gst-meta-audio"

RDEPENDS_task-poky-nfs-server = "\
    nfs-utils"

# rpcinfo can be useful
RRECOMMENDS_task-poky-nfs-server = "\
    glibc-utils"

#    minimo \
#    teleport \
#    xst \
#    libgtkstylus \
#    xrdb \
