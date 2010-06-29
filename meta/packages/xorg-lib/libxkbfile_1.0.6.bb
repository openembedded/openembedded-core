DESCRIPTION = "X11 keyboard file manipulation library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=aa32e1b87f1da02948ab7b55e129bf5f"

DEPENDS += "virtual/libx11 kbproto"

PR = "r0"
PE = "1"

BBCLASSEXTEND = "native"
