DESCRIPTION = "Qemu helper scripts from Poky"
LICENSE = "GPL"
RDEPENDS = "qemu-nativesdk"
PR = "r7"

FILESPATH = "${FILE_DIRNAME}/qemu-helper"

SRC_URI = "file://${POKYBASE}/scripts/poky-qemu \
           file://${POKYBASE}/scripts/poky-qemu-internal \
           file://${POKYBASE}/scripts/poky-addptable2image \
           file://${POKYBASE}/scripts/poky-qemu-ifup \
           file://${POKYBASE}/scripts/poky-qemu-ifdown \
           file://${POKYBASE}/scripts/poky-find-native-sysroot \
           file://${POKYBASE}/scripts/poky-extract-sdk \
           file://${POKYBASE}/scripts/poky-export-rootfs \
           file://tunctl.c \
           file://raw2flash.c \
          "

S = "${WORKDIR}"

inherit nativesdk

do_compile() {
	${CC} tunctl.c -o tunctl
	${CC} raw2flash.c -o raw2flash.spitz
	${CC} raw2flash.c -o flash2raw.spitz -Dflash2raw
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}${POKYBASE}/scripts/poky-* ${D}${bindir}/
	install tunctl ${D}${bindir}/
	install raw2flash.spitz ${D}${bindir}/
	install flash2raw.spitz ${D}${bindir}/
	ln -fs raw2flash.spitz ${D}${bindir}/raw2flash.akita
	ln -fs raw2flash.spitz ${D}${bindir}/raw2flash.borzoi
	ln -fs raw2flash.spitz ${D}${bindir}/raw2flash.terrier
	ln -fs flash2raw.spitz ${D}${bindir}/flash2raw.akita
	ln -fs flash2raw.spitz ${D}${bindir}/flash2raw.borzoi
	ln -fs flash2raw.spitz ${D}${bindir}/flash2raw.terrier
}
