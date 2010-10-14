SDK-GMAE = " \
    dbus-dev \
    dbus-glib-dev \
    gtk+-dev \
    gstreamer-dev \
    bluez4-dev \
    gconf-dbus-dev \
    avahi-dev \
    telepathy-glib-dev \
    eds-dbus-dev \
    libecal-dev \
    libebook-dev \
    libxi-dev \
    libsqlite3-dev \
    "

# TODO: gvfs-dev, mission-control

SDK-EXTRAS = ""
SDK-EXTRAS_qemux86 = " valgrind lttng-ust"
SDK-EXTRAS_qemux86-64 = " lttng-ust"
SDK-EXTRAS_qemuppc = " lttng-ust"
SDK-EXTRAS_emenlow = " lttng-ust"
SDK-EXTRAS_atom-pc = " lttng-ust"
