DESCRIPTION = "Qemu helper utilities from Poky"
LICENSE = "GPLv2"
RDEPENDS = "qemu-native"
PR = "r0"

FILESPATH = "${FILE_DIRNAME}/qemu-helper"

SRC_URI = "file://tunctl.c"

S = "${WORKDIR}"

inherit native

do_compile() {
	${CC} tunctl.c -o tunctl
}

do_install() {
	install -d ${D}${bindir}
	install tunctl ${D}${bindir}/
}
