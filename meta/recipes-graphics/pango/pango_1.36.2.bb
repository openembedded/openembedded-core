require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

GNOME_COMPRESS_TYPE="xz"

SRC_URI += "file://no-tests.patch \
            file://multilib-fix-clean.patch \
"

SRC_URI[archive.md5sum] = "253026c7132c22e52cefd998ba89a742"
SRC_URI[archive.sha256sum] = "f07f9392c9cf20daf5c17a210b2c3f3823d517e1917b72f20bb19353b2bc2c63"
