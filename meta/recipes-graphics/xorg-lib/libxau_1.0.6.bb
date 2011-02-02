SUMMARY = "Xau: X Authority Database library"

DESCRIPTION = "libxau provides the main interfaces to the X11 \
authorisation handling, which controls authorisation for X connections, \
both client-side and server-side."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7908e342491198401321cec1956807ec"

DEPENDS += " xproto gettext"
PROVIDES = "xau"

PR = "r0"
PE = "1"

XORG_PN = "libXau"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "4a2cbd83727682f9ee1c1e719bac6adb"
SRC_URI[sha256sum] = "ee84415ffedcc1c0d39b3a39c35d0596c89907bba93d22bf85e24d21f90170fc"
