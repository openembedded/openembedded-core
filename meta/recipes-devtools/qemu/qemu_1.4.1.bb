require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://fdt_header.patch"

SRC_URI_prepend = "http://wiki.qemu.org/download/qemu-${PV}.tar.bz2"
SRC_URI[md5sum] = "eb2d696956324722b5ecfa46e41f9a75"
SRC_URI[sha256sum] = "75063a9326221607de3599e89fb5af80f2d8080ddc55ca253fff113843432df1"
