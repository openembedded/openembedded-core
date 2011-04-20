require gst-fluendo.inc
DESCRIPTION = "Fluendo closed-format mpeg video GStreamer plug-in"

LICENSE = "MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=be282f1c3cc9a98cc0dc5c2b25dfc510 \
                    file://src/gstmpegdemux.h;beginline=1;endline=19;md5=a9e90033f59897b91664d9f2a2ff01dd"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

PR = "r0"

SRC_URI[md5sum] = "4ee0353eaa386f9b1c5570298d204357"
SRC_URI[sha256sum] = "410897527f8bd8f007c9915c9450113c9e73c76f89aee3d36da825422a83a621"
