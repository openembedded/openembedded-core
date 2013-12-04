require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch \
"

SRC_URI[archive.md5sum] = "9e0d3a1ea395172f8c39ba98a4d2081a"
SRC_URI[archive.sha256sum] = "42e4b51cdc99e6878a9ea2a5ef2b31b79c1033f8518726df738a3c54c90e59f8"
