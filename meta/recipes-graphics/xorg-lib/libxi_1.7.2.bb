require xorg-lib-common.inc

SUMMARY = "XI: X Input extension library"

DESCRIPTION = "libxi is an extension to the X11 protocol to support \
input devices other than the core X keyboard and pointer.  It allows \
client programs to select input from these devices independently from \
each other and independently from the core devices."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=17b064789fab936a1c58c4e13d965b0f \
                    file://src/XIGetDevFocus.c;endline=23;md5=cdfb0d435a33ec57ea0d1e8e395b729f"

DEPENDS += "libxext inputproto libxfixes"

PE = "1"

XORG_PN = "libXi"

SRC_URI[md5sum] = "f4df3532b1af1dcc905d804f55b30b4a"
SRC_URI[sha256sum] = "df24781dc63645e2b561cd0b20bd8a0e7aff02e426a8d2a7641159004d4cb20e"
