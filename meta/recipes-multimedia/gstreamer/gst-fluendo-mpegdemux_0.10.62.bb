require gst-fluendo.inc
DESCRIPTION = "Fluendo closed-format mpeg video GStreamer plug-in"

LICENSE = "MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=be282f1c3cc9a98cc0dc5c2b25dfc510 \
                    file://src/gstmpegdemux.h;beginline=1;endline=19;md5=a9e90033f59897b91664d9f2a2ff01dd"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

PR = "r0"

SRC_URI[md5sum] = "de2a22760f73ba678b7cb51e9f94bc21"
SRC_URI[sha256sum] = "c4ba24502f59849fc2f3948389213c99e23a0986506111cc22cce3a64a33ac05"
