require xorg-app-common.inc

DESCRIPTION = "X Resize and Rotate extension command."
LICENSE= "BSD-X"
DEPENDS += "libxrandr libxrender"
PR = "r1"
PE = "1"

SRC_URI += "file://resolve_symbol_clash.patch;patch=1"
