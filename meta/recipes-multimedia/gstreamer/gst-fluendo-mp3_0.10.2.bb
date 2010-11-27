require gst-fluendo.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=086bf92d974e459276adbfd808c615b4"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

SRC_URI += "file://configure_fix.patch;patch=1"

PR = "r1"

DESCRIPTION = "Fluendo closed-format mp3 GStreamer plug-in"
