require gst-fluendo.inc

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

SRC_URI += "file://configure_fix.patch;patch=1"

PR = "r1"

DESCRIPTION = "Fluendo closed-format mp3 GStreamer plug-in"
