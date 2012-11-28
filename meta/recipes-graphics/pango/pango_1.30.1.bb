require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

PR = "r0"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch"

SRC_URI[archive.md5sum] = "ec3c1f236ee9bd4a982a5f46fcaff7b9"
SRC_URI[archive.sha256sum] = "3a8c061e143c272ddcd5467b3567e970cfbb64d1d1600a8f8e62435556220cbe"

#PARALLEL_MAKE = ""
