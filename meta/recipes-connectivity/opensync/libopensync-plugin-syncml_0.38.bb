require libopensync-plugin_0.36.inc
LICENSE = "LGPL"

DEPENDS += " libsyncml (>= 0.4.7)"

SRC_URI += "file://fixerror.patch;patch=1"

PR = "r1"
