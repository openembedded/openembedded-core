SDK-GMAE = " \
    dbus-dev \
    dbus-glib-dev \
    gtk+-dev \
    gstreamer-dev \
    bluez4-dev \
    gconf-dev \
    avahi-dev \
    telepathy-glib-dev \
    libxi-dev \
    libsqlite3-dev \
    gnome-desktop \
    libart-lgpl \
    libtelepathy \
    pulseaudio \
    telepathy-glib \
    telepathy-idle \
    telepathy-mission-control \
    telepathy-python \
    "

# TODO: gvfs-dev, mission-control

SDK-EXTRAS ?= ""
SDK-EXTRAS_qemux86 ?= " valgrind lttng-ust-dev"
SDK-EXTRAS_qemux86-64 ?= " lttng-ust-dev"
SDK-EXTRAS_qemuppc ?= " lttng-ust-dev"
SDK-EXTRAS_qemuarm ?= " lttng-ust-dev"
