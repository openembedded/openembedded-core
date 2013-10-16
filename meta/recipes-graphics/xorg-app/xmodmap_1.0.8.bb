require xorg-app-common.inc

SUMMARY = "Utility for modifying keymaps and pointer button mappings in X"

DESCRIPTION = "The xmodmap program is used to edit and display the \
keyboard modifier map and keymap table that are used by client \
applications to convert event keycodes into keysyms. It is usually run \
from the user's session startup script to configure the keyboard \
according to personal tastes."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=272c17e96370e1e74773fa22d9989621"

PE = "1"

SRC_URI += "file://gnu-source.patch"

SRC_URI[md5sum] = "5511da3361eea4eaa21427652c559e1c"
SRC_URI[sha256sum] = "efe2e3c89858a2db3bdcf969f55f55d0af4f5007789198344de0595249a99fc3"
