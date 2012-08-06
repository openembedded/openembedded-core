require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

PR = "r0"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch"

SRC_URI[archive.md5sum] = "2a70627ffd9f43c52c04cc0b05fe359f"
SRC_URI[archive.sha256sum] = "7c6d2ab024affaed0e942f9279b818235f9c6a36d9fc50688f48d387f4102dff"

#PARALLEL_MAKE = ""
