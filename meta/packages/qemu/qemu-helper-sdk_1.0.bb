DESCRIPTION = "Qemu helper scripts from Poky"
LICENSE = "GPL"
RDEPENDS = "qemu-sdk"
PR = "r6"

FILESPATH = "${FILE_DIRNAME}/qemu-helper"

SRC_URI = "file://${OEROOT}/scripts/poky-qemu \
           file://${OEROOT}/scripts/poky-qemu-internal \
           file://${OEROOT}/scripts/poky-addptable2image \
           file://${OEROOT}/scripts/poky-qemu-ifup \
	   file://raw2flash.c"
		      
S = "${WORKDIR}"
		      
inherit sdk

do_compile() {
	${CC} raw2flash.c -o raw2flash.spitz
	${CC} raw2flash.c -o flash2raw.spitz -Dflash2raw
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 poky-* ${D}${bindir}/
	install raw2flash.spitz ${D}${bindir}/
	install flash2raw.spitz ${D}${bindir}/
	ln -fs raw2flash.spitz ${D}${bindir}/raw2flash.akita
	ln -fs raw2flash.spitz ${D}${bindir}/raw2flash.borzoi
	ln -fs raw2flash.spitz ${D}${bindir}/raw2flash.terrier
	ln -fs flash2raw.spitz ${D}${bindir}/flash2raw.akita
	ln -fs flash2raw.spitz ${D}${bindir}/flash2raw.borzoi
	ln -fs flash2raw.spitz ${D}${bindir}/flash2raw.terrier
}
