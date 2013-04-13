require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://3f08ffb4a4741d147634761dc053ed386243a0de.patch \
            file://fdt_header.patch \
           "

SRC_URI_prepend = "http://wiki.qemu.org/download/qemu-${PV}.tar.bz2"
SRC_URI[md5sum] = "78f13b774814b6b7ebcaf4f9b9204318"
SRC_URI[sha256sum] = "066297ed77408fb7588889c271a85cf3c259ad55c939315988e6062d7708eda8"
