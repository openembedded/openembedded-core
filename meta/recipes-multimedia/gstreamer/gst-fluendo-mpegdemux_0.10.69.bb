require gst-fluendo.inc
DESCRIPTION = "Fluendo closed-format mpeg video GStreamer plug-in"

LICENSE = "MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=be282f1c3cc9a98cc0dc5c2b25dfc510 \
                    file://src/gstmpegdemux.h;beginline=1;endline=19;md5=a9e90033f59897b91664d9f2a2ff01dd"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

PR = "r0"

SRC_URI[md5sum] = "098105ecb68779f34097faf35214fc97"
SRC_URI[sha256sum] = "a62a1249f05c5b6d9d0ab31a0d5cb15f2682587dec0050fc57c1102a6d76b32d"
