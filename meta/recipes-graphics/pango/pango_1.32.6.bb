require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch \
"

SRC_URI[archive.md5sum] = "1af2e3a0ac5a258eff5ceddb6ed60ebd"
SRC_URI[archive.sha256sum] = "8e9a3eadebf30a31640f2b3ae0fb455cf92d10d1cad246d0ffe72ec595905174"
