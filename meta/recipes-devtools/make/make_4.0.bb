LICENSE = "GPLv3 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://tests/COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://glob/COPYING.LIB;md5=4a770b67e6be0f60da244beb2de0fce4"
require make.inc

EXTRA_OECONF += "--without-guile"

SRC_URI[md5sum] = "571d470a7647b455e3af3f92d79f1c18"
SRC_URI[sha256sum] = "e60686c7afede62cc8c86ad3012cf081ea4887daf9d223ce7115703b2bb2dbdb"

BBCLASSEXTEND = "native nativesdk"
