DESCRIPTION = "Tasks for OpenedHand Poky"
PR = "r44"

PACKAGES = "\
    task-oh-base \
    task-oh-base-dbg \
    task-oh-base-dev \
    task-oh-boot \
    task-oh-boot-dbg \
    task-oh-boot-dev \
    task-oh-standard \
    task-oh-standard-dbg \
    task-oh-standard-dev \
    task-oh-boot-extras \
    task-oh-boot-extras-dbg \
    task-oh-boot-extras-dev \
    task-oh-boot-min-extras \
    task-oh-boot-min-extras-dbg \
    task-oh-boot-min-extras-dev \
    task-oh-devtools \
    task-oh-devtools-dbg \
    task-oh-devtools-dev \
    task-oh-testapps \
    task-oh-testapps-dbg \
    task-oh-testapps-dev"

XSERVER ?= "xserver-kdrive-fbdev"

ALLOW_EMPTY = "1"

RDEPENDS_task-oh-boot = "\
    base-files \
    base-passwd \
    busybox \
    initscripts \
    netbase \
    sysvinit \
    sysvinit-pidof \
    tinylogin \
    modutils-initscripts \
    fuser \
    setserial \
    ipkg \
    update-alternatives \
    module-init-tools-depmod"

RDEPENDS_task-oh-boot-extras = "\
    task-base"

RDEPENDS_task-oh-boot-min-extras = "\
    task-base-oh-minimal"

RDEPENDS_task-oh-base = "\
    psplash \
    matchbox-common \
    matchbox-wm \
    matchbox-keyboard \
    matchbox-panel \
    ${XSERVER} \
    xserver-kdrive-common \
    xserver-nodm-init \
    ttf-bitstream-vera \
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

RDEPENDS_task-oh-standard = "\
    leafpad \
    dropbear \
    portmap \
    matchbox-desktop \
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
    usbinit \
    settings-daemon \
    gtk-clearlooks-engine \
    eds-dbus \
    contacts \
    dates \
    web \
    pcmanfm \
    puzzles \
    kf \
    rxvt-unicode \
    avahi-daemon \
    gnome-vfs-plugin-dbus \
    gnome-vfs-plugin-file \
    gnome-vfs-plugin-http \
    gtk-theme-clearlooks"

RDEPENDS_task-oh-devtools = "\
    oprofile \
    gdb \    
    strace \
    less \
    lttng-viewer"
RRECOMMENDS_task-oh-devtools = "\
    kernel-module-oprofile"

RDEPENDS_task-oh-testapps = "\
    tslib-calibrate \
    tslib-tests \
    lrzsz \
    alsa-utils-amixer \
    alsa-utils-aplay \
    owl-video-widget \
    gst-meta-video \
    gst-meta-audio"


#    minimo \
#    teleport \
#    xst \
#    libgtkstylus \
#    xrdb \
