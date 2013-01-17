require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

PR = "r2"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch \
            file://obsolete_automake_macros.patch \
"

SRC_URI[archive.md5sum] = "8e846804d6e219bc795a26a4a39b5bfd"
SRC_URI[archive.sha256sum] = "7d7bc99c3d9b580cb4fe787fe47824e56e39534b9040e1c8a2a159248d8e5821"

#PARALLEL_MAKE = ""
