DESCRIPTION = "File attempts to classify files depending \
on their contents and prints a description if a match is found."
HOMEPAGE = "http://www.darwinsys.com/file/"
SECTION = "console/utils"

# two clause BSD
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;beginline=2;md5=6a7382872edb68d33e1a9398b6e03188"

DEPENDS = "zlib file-native"
DEPENDS_virtclass-native = "zlib-native"
PR = "r0"

SRC_URI = "ftp://ftp.astron.com/pub/file/file-${PV}.tar.gz \
           file://dump \
           file://filesystems"

inherit autotools

do_configure_prepend() {
	cp ${WORKDIR}/dump ${S}/magic/Magdir/
	cp ${WORKDIR}/filesystems ${S}/magic/Magdir/
}

FILES_${PN} += "${datadir}/misc/*.mgc"

BBCLASSEXTEND = "native"
