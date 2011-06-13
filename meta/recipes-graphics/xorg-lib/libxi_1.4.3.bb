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

SRC_URI[md5sum] = "792e8a7ddc7175911d69f823d38eaff6"
SRC_URI[sha256sum] = "08c9d89a404740592621e6f5078414df86ccc78ca876e3da1af15639d81c74e0"
