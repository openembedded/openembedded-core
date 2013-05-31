SUMMARY = "File classification tool"
DESCRIPTION = "File attempts to classify files depending \
on their contents and prints a description if a match is found."
HOMEPAGE = "http://www.darwinsys.com/file/"
SECTION = "console/utils"

# two clause BSD
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;beginline=2;md5=6a7382872edb68d33e1a9398b6e03188"

DEPENDS = "zlib file-native"
DEPENDS_class-native = "zlib-native"
PR = "r0"

SRC_URI = "ftp://ftp.astron.com/pub/file/file-${PV}.tar.gz \
           file://dump \
           file://filesystems"

SRC_URI[md5sum] = "c26625f1d6773ad4bc5a87c0e315632c"
SRC_URI[sha256sum] = "461ba01dd0900a3b7c2dcd901b04903192c07887a9c1928af0be507572c77fe4"

inherit autotools

do_configure_prepend() {
	cp ${WORKDIR}/dump ${S}/magic/Magdir/
	cp ${WORKDIR}/filesystems ${S}/magic/Magdir/
}

FILES_${PN} += "${datadir}/misc/*.mgc"

do_install_append_class-native() {
	create_cmdline_wrapper ${D}/${bindir}/file \
		--magic-file ${datadir}/misc/magic.mgc
}


BBCLASSEXTEND = "native"
