require xorg-app-common.inc

SUMMARY = "X Event Viewer"
DESCRIPTION = "Xev creates a window and then asks the X server to send it events \
whenever anything happens to the window (such as it being moved, resized, \
typed in, clicked in, etc.). You can also attach it to an existing window."

LIC_FILES_CHKSUM = "file://xev.c;beginline=1;endline=33;md5=577c99421f1803b891d2c79097ae4682"
LICENSE = "MIT"

PE = "1"

DEPENDS += "libxrandr xproto"

SRC_URI += "file://diet-x11.patch"

SRC_URI[md5sum] = "5b0a0b6f589441d546da21739fa75634"
SRC_URI[sha256sum] = "11f17fab097f17d9efc51ea9d0e3140bea3904eb3c486afa7c8c3eedab496243"
