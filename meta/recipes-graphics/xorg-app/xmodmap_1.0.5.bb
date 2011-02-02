require xorg-app-common.inc

SUMMARY = "Utility for modifying keymaps and pointer button mappings in X"

DESCRIPTION = "The xmodmap program is used to edit and display the \
keyboard modifier map and keymap table that are used by client \
applications to convert event keycodes into keysyms. It is usually run \
from the user's session startup script to configure the keyboard \
according to personal tastes."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=eef098b27f09d0ac39268df0cc2c00b5"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "b18850d373f3717dca569377c449d091"
SRC_URI[sha256sum] = "421ee7a17b0cf559c4901225f70c497e3c5ca205ec7dcc84712055818d2f3402"
