require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://run-ptest \
            file://multilib-fix-clean.patch \
"

SRC_URI[archive.md5sum] = "2500930093c3ed38acb40e4255bce2f1"
SRC_URI[archive.sha256sum] = "be0e94b2e5c7459f0b6db21efab6253556c8f443837200b8736d697071276ac8"