require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI_prepend = "http://wiki.qemu.org/download/qemu-${PV}.tar.bz2"
SRC_URI[md5sum] = "a4030ddd2ba324152a97d65d3c0b247d"
SRC_URI[sha256sum] = "878055ec05bc28fecfe2da97eb8bc992e8635575b67cebdfc5ca1ede171140a8"

PR = "r0"
