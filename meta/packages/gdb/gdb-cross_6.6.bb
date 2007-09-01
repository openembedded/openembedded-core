require gdb-cross.inc

SRC_URI += "file://sim-install-6.6.patch;patch=1"

inherit cross

PR = "r1"