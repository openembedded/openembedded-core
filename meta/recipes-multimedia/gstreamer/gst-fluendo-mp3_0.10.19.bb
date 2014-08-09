require gst-fluendo.inc

SUMMARY = "Fluendo closed-format mp3 GStreamer plug-in"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=259a43dd1c9854b71fc396f74699f4d2"
LICENSE_FLAGS = "commercial"

GSTREAMER_DEBUG ?= "--disable-debug"
EXTRA_OECONF += "${GSTREAMER_DEBUG}"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

SRC_URI[md5sum] = "5d95a9a216dd15bc5c00c9414061115c"
SRC_URI[sha256sum] = "30c79d24d8926f75dd4ef0f572942ce155ad541734b36a95591b9c0524dcc0f3"
