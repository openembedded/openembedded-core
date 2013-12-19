require gst-fluendo.inc

SUMMARY = "Fluendo closed-format mpeg video GStreamer plug-in"
LICENSE = "MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=be282f1c3cc9a98cc0dc5c2b25dfc510 \
                    file://src/gstmpegdemux.h;beginline=1;endline=19;md5=a9e90033f59897b91664d9f2a2ff01dd"
LICENSE_FLAGS = "commercial"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

SRC_URI[md5sum] = "df726579404af65b9536428661ab4322"
SRC_URI[sha256sum] = "a9784bc16352d0fb73de81b9c17142609e8bede46f6d9b881fc3d19673954abf"
