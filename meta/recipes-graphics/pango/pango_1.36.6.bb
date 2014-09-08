require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://run-ptest \
            file://multilib-fix-clean.patch \
"

SRC_URI[archive.md5sum] = "1c27523c3f3a4efe4d9d303d0d240320"
SRC_URI[archive.sha256sum] = "4c53c752823723875078b91340f32136aadb99e91c0f6483f024f978a02c8624"