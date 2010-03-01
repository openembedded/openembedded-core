require xserver-xf86-lite.inc

PR = "r1"

DEPENDS += "font-util"

SRC_URI += "file://nodolt.patch;patch=1 \
            file://crosscompile.patch;patch=1"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1 \
            file://revert_make_sys_c_use_unaligned_access_functions.patch;patch=1"
