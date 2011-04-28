require xorg-lib-common.inc

SUMMARY = "XI: X Input extension library"

DESCRIPTION = "libxi is an extension to the X11 protocol to support \
input devices other than the core X keyboard and pointer.  It allows \
client programs to select input from these devices independently from \
each other and independently from the core devices."

DEPENDS += "libxext inputproto"
SRCREV = "d0326fe8cdbb08d4f52d79fd3fd4e1b2a0951d5e"
PE = "1"
PV = "1.2.99.5+gitr${SRCPV}"

XORG_PN = "libXi"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/${XORG_PN};protocol=git"
S = "${WORKDIR}/git"
