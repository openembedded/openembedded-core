SUMMARY = "Xau: X Authority Database library"

DESCRIPTION = "libxau provides the main interfaces to the X11 \
authorisation handling, which controls authorisation for X connections, \
both client-side and server-side."

require xorg-lib-common.inc

inherit gettext

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7908e342491198401321cec1956807ec"

DEPENDS += " xproto"
PROVIDES = "xau"

PR = "r0"
PE = "1"

XORG_PN = "libXau"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "2d241521df40d27034413436d1a1465c"
SRC_URI[sha256sum] = "7153ba503e2362d552612d9dc2e7d7ad3106d5055e310a26ecf28addf471a489"
