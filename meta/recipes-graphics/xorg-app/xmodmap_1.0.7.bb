require xorg-app-common.inc

SUMMARY = "Utility for modifying keymaps and pointer button mappings in X"

DESCRIPTION = "The xmodmap program is used to edit and display the \
keyboard modifier map and keymap table that are used by client \
applications to convert event keycodes into keysyms. It is usually run \
from the user's session startup script to configure the keyboard \
according to personal tastes."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=eef098b27f09d0ac39268df0cc2c00b5"

PR = "${INC_PR}.0"
PE = "1"

SRC_URI += "file://gnu-source.patch"

SRC_URI[md5sum] = "d9b65f6881afe0d6d9863b30e1081bde"
SRC_URI[sha256sum] = "ef22ede9c4a3c720da539292c6911515a8408e618e0dec6aa2196ee2153de4b5"
