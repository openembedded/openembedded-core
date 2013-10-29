require xorg-proto-common.inc

SUMMARY = "XFont: X Font rasterisation headers"

DESCRIPTION = "This package provides the wire protocol for the X Font \
rasterisation extensions.  These extensions are used to control \
server-side font configurations."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=c3e48aa9ce868c8e90f0401db41c11a2 \
                    file://FSproto.h;endline=44;md5=d2e58e27095e5ea7d4ad456ccb91986c"

PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "c5f4f1fb4ba7766eedbc9489e81f3be2"
SRC_URI[sha256sum] = "869c97e5a536a8f3c9bc8b9923780ff1f062094bab935e26f96df3d6f1aa68a9"
