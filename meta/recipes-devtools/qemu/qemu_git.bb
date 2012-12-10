require qemu.inc

SRCREV = "6d6c9f59ca1b1a76ade7ad868bef191818f58819"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PV = "1.3.0+git${SRCPV}"
PR = "r0"

SRC_URI_prepend = "git://git.qemu.org/qemu.git;protocol=git"
S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"
