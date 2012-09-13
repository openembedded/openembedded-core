require gst-fluendo.inc
DESCRIPTION = "Fluendo closed-format mpeg video GStreamer plug-in"

LICENSE = "MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=be282f1c3cc9a98cc0dc5c2b25dfc510 \
                    file://src/gstmpegdemux.h;beginline=1;endline=19;md5=a9e90033f59897b91664d9f2a2ff01dd"
LICENSE_FLAGS = "commercial"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

PR = "r0"

SRC_URI[md5sum] = "a7b5152cd73fd0f9fa653fca93c82bd7"
SRC_URI[sha256sum] = "3be3907e860bce4cc8bee0c70e4b28e91d97f4037e611473b8c9bb954b4de4f9"
