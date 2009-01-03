require xorg-app-common.inc

DESCRIPTION = "The X Keyboard Extension essentially replaces the core protocol definition of keyboard."

DEPENDS += "libxkbfile"

BBCLASSEXTEND = "native"
