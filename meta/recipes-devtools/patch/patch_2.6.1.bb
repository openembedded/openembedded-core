require patch.inc
LICENSE = "GPLv3"

PR = "r0"

SRC_URI += " file://global-reject-file.diff "

SRC_URI[md5sum] = "d758eb96d3f75047efc004a720d33daf"
SRC_URI[sha256sum] = "d1563731e9cffed11cc5f011b2b8e074c325e86a383a91889b5c5b80b09781b9"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

acpaths = "-I ${S}/gl/m4 -I ${S}/m4 "

