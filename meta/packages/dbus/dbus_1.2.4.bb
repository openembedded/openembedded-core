include dbus.inc

PR = "r1"

inherit update-rc.d

SRC_URI += "file://fix-install-daemon.patch;patch=1 "
