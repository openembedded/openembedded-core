require util-linux.inc

SRC_URI += "file://util-linux_2.12r-12.diff.gz;patch=1"
SRC_URI += "file://glibc-fix.patch;patch=1"
SRC_URI += "file://debian-bug392236.patch;patch=1"

PR = "r11"
