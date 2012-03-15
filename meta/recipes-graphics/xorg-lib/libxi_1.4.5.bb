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

EXTRA_OECONF_append = " --enable-specs=no"

SRC_URI[md5sum] = "82dcdc76388116800a2c3ad969f510a4"
SRC_URI[sha256sum] = "22a99123229d22e6e1567c4cda0224a744475f427625d61b23d965157a86f1b5"
