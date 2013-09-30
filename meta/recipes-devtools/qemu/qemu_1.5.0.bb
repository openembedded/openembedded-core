require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://fdt_header.patch \
            file://target-i386-Fix-aflag-logic-for-CODE64-and-the-0x67-.patch \
            file://target-ppc_fix_bit_extraction.patch \
            file://fxrstorssefix.patch \
           "

SRC_URI_prepend = "http://wiki.qemu.org/download/qemu-${PV}.tar.bz2"
SRC_URI[md5sum] = "b6f3265b8ed39d77e8f354f35cc26e16"
SRC_URI[sha256sum] = "b22b30ee9712568dfb4eedf76783f4a76546e1cbc41659b909646bcf0b4867bb"

COMPATIBLE_HOST_class-target_mips64 = "null"
