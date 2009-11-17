require gdb-cross.inc

inherit cross

SRC_URI += "file://sim-install-6.6.patch;patch=1"

PR = "r2"
