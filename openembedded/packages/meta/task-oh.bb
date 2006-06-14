PACKAGES = "task-oh-base task-oh-devel task-oh-boot task-oh-standard task-oh-boot-extras task-oh-boot-min-extras"
DESCRIPTION = "Tasks for OpenedHand Poky"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r35"

ALLOW_EMPTY = "1"

RDEPENDS_task-oh-boot := "\
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
    linux-hotplug \
    ipkg \
    module-init-tools-depmod"

RDEPENDS_task-oh-boot-extras := "\
    ${BOOTSTRAP_EXTRA_RDEPENDS}"

RDEPENDS_task-oh-boot-min-extras := "\
    kernel \
    udev \
    sysfsutils \
    ${PCMCIA_MANAGER} \
    apm \
    udev-utils"

RDEPENDS_task-oh-base := "\
    matchbox-common \
    matchbox-wm \
    matchbox-keyboard \
    matchbox-panel    \
    xserver-kdrive-common \
    xserver-nodm-init \
    ttf-bitstream-vera \
    xauth \
    xhost \
    udev \
    sysfsutils \
    gdk-pixbuf-loader-png \
    gdk-pixbuf-loader-gif \
    gdk-pixbuf-loader-xpm \
    gdk-pixbuf-loader-jpeg \
    pango-module-basic-x \
    pango-module-basic-fc \
    gtk+ "

RDEPENDS_task-oh-standard := "\
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
    xset \
    rxvt-unicode \
    avahi-daemon \
    gnome-vfs-plugin-dbus \
    gnome-vfs-plugin-file \
    gnome-vfs-plugin-http \
    gtk-theme-clearlooks"

RDEPENDS_task-oh-standard_append_c7x0 = " zaurusd "
RDEPENDS_task-oh-standard_append_spitz = " zaurusd "
RDEPENDS_task-oh-standard_append_akita = " zaurusd "

PACKAGE_ARCH_task-oh-standard = "${MACHINE_ARCH}"

#    minimo \
#    teleport \
#    xst \
#    libgtkstylus \
#    detect-stylus \
#    xrdb \


RDEPENDS_task-oh-devel := "\
    tslib-calibrate \
    tslib-tests \
    oprofile \
    gdb \    
    strace \
    lttng-viewer"

RRECOMMENDS_task-oh-devel := "\
    kernel-module-oprofile"
#    lttng-modules


