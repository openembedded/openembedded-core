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
PR = "r1"

XORG_PN = "libXi"

SRC_URI[md5sum] = "78ee882e1ff3b192cf54070bdb19938e"
SRC_URI[sha256sum] = "f2e3627d7292ec5eff488ab58867fba14a62f06e72a8d3337ab6222c09873109"
