require xorg-util-common.inc

DESCRIPTION = "C preprocessor interface to the make utility"
DEPENDS = "util-macros"
RDEPENDS_${PN} = "perl xproto"

LIC_FILES_CHKSUM = "file://COPYING;md5=b9c6cfb044c6d0ff899eaafe4c729367"

PR = "r1"
PE = "1"

SRC_URI[md5sum] = "0fd1e53d94142ddee5340f87de0b9561"
SRC_URI[sha256sum] = "68038fa67929c5553044ad7417e1a64cabe954f04213b305dd36a04a61317d31"
