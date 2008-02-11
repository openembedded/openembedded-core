DESCRIPTION = "Qemu helper scripts from Poky"
LICENSE = "GPL"
RDEPENDS = "qemu-sdk"
PR = "r5"

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
	ln -fs raw2flash.spitz raw2flash.akita
	ln -fs raw2flash.spitz raw2flash.borzoi
	ln -fs raw2flash.spitz raw2flash.terrier
	${CC} raw2flash.c -o flash2raw.spitz -Dflash2raw
	ln -fs flash2raw.spitz flash2raw.akita
	ln -fs flash2raw.spitz flash2raw.borzoi
	ln -fs flash2raw.spitz flash2raw.terrier
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 poky-* ${D}${bindir}/
	install -m 0755 *.akita ${D}${bindir}/
	install -m 0755 *.spitz ${D}${bindir}/
	install -m 0755 *.borzoi ${D}${bindir}/
	install -m 0755 *.terrier ${D}${bindir}/
}
