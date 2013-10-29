require qemu.inc

SRCREV = "04024dea2674861fcf13582a77b58130c67fccd8"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PV = "1.3.0+git${SRCPV}"

SRC_URI_prepend = "git://git.qemu.org/qemu.git"
S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"

COMPATIBLE_HOST_class-target_mips64 = "null"
