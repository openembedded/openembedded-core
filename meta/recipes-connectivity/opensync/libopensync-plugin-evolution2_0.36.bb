require libopensync-plugin_0.36.inc
LICENSE = "LGPL"

DEPENDS += " eds-dbus"

SRC_URI += "file://0.37-fixes.patch;patch=1"

PR = "r1"
