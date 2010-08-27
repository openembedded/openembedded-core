require cpio_v2.inc

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b7f772ea3a2489231cb4872656cac34b"

PR = "r0"

SRC_URI += "file://m4extensions.patch"

SRC_URI[md5sum] = "0caa356e69e149fb49b76bacc64615a1"
SRC_URI[sha256sum] = "601b1d774cd6e4cd39416203c91ec59dbd65dd27d79d75e1a9b89497ea643978"

# Required to build with gcc 4.3 and later:
CFLAGS += "-fgnu89-inline"
