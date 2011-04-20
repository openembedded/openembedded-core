DESCRIPTION = "Qemu helper scripts from Poky"
LICENSE = "GPLv2"
RDEPENDS_${PN} = "qemu-nativesdk"
PR = "r9"

FILESPATH = "${FILE_DIRNAME}/qemu-helper"

LIC_FILES_CHKSUM = "file://${WORKDIR}/tunctl.c;endline=4;md5=ff3a09996bc5fff6bc5d4e0b4c28f999 \
                    file://${COREBASE}/scripts/runqemu;endline=18;md5=ce52af3dd295e20ad1d849611b83690b"


SRC_URI = "file://${COREBASE}/scripts/runqemu \
           file://${COREBASE}/scripts/runqemu-internal \
           file://${COREBASE}/scripts/poky-addptable2image \
           file://${COREBASE}/scripts/poky-gen-tapdevs \
           file://${COREBASE}/scripts/runqemu-ifup \
           file://${COREBASE}/scripts/runqemu-ifdown \
           file://${COREBASE}/scripts/poky-find-native-sysroot \
           file://${COREBASE}/scripts/poky-extract-sdk \
           file://${COREBASE}/scripts/poky-export-rootfs \
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
	install -m 0755 ${WORKDIR}${COREBASE}/scripts/poky-* ${D}${bindir}/
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
