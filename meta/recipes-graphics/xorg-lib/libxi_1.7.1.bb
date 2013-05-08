require xorg-lib-common.inc

SUMMARY = "XI: X Input extension library"

DESCRIPTION = "libxi is an extension to the X11 protocol to support \
input devices other than the core X keyboard and pointer.  It allows \
client programs to select input from these devices independently from \
each other and independently from the core devices."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=17b064789fab936a1c58c4e13d965b0f \
                    file://src/XIGetDevFocus.c;endline=23;md5=cdfb0d435a33ec57ea0d1e8e395b729f"

DEPENDS += "libxext inputproto"

PE = "1"
PR = "r0"

XORG_PN = "libXi"

SRC_URI[md5sum] = "24d71afed1b86c60d4eb361628d7f47b"
SRC_URI[sha256sum] = "e92adb6b69c53c51e05c1e65db97e23751b935a693000fb0606c11b88c0066c5"
