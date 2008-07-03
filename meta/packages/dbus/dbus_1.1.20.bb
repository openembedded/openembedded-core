include dbus.inc

PR = "r4"

SRC_URI += "file://fix-dbus-launch-x11.patch;patch=1 \
            file://fix-install-daemon.patch;patch=1 "
