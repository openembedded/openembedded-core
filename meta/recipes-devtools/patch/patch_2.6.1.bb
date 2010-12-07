require patch.inc
LICENSE = "GPLv3"

PR = "r0"

SRC_URI += " file://global-reject-file.diff;patch=1 "

SRC_URI[md5sum] = "d758eb96d3f75047efc004a720d33daf"
SRC_URI[sha256sum] = "ecb5c6469d732bcf01d6ec1afe9e64f1668caba5bfdb103c28d7f537ba3cdb8a"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

acpaths = "-I ${S}/gl/m4 -I ${S}/m4 "

