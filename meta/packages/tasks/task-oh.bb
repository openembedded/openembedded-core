DESCRIPTION = "Tasks for OpenedHand Poky"
PR = "r42"

PACKAGES = "\
    task-oh-base \
    task-oh-boot \
    task-oh-standard \
    task-oh-boot-extras \
    task-oh-boot-min-extras \
    task-oh-devtools \
    task-oh-testapps"

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
    update-alternatives \
    module-init-tools-depmod"
#    linux-hotplug \

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
