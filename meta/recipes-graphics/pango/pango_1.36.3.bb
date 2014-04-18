require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch \
"

SRC_URI[archive.md5sum] = "d9532826e95bdb374355deebc42441bb"
SRC_URI[archive.sha256sum] = "ad48e32917f94aa9d507486d44366e59355fcfd46ef86d119ddcba566ada5d22"
