require gst-fluendo.inc
DESCRIPTION = "Fluendo closed-format mpeg video GStreamer plug-in"

LICENSE = "MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=be282f1c3cc9a98cc0dc5c2b25dfc510 \
                    file://src/gstmpegdemux.h;beginline=1;endline=19;md5=a9e90033f59897b91664d9f2a2ff01dd"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

PR = "r0"

SRC_URI[md5sum] = "ceb9001be71e8cbc3e97f9b46c9b611e"
SRC_URI[sha256sum] = "b46f4a05f621e9742d22ac5c835c2f5d9aa495845b5c783475c03d1c46f47727"
