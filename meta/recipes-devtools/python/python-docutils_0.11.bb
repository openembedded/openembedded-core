SUMMARY = "Text processing system for documentation"
HOMEPAGE = "http://docutils.sourceforge.net"
SECTION = "devel/python"
LICENSE = "PSF & BSD-2-Clause & GPLv3"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=da0d261d1db78ab21ce86c79364a4098"

DEPENDS = "python"

SRC_URI = "${SOURCEFORGE_MIRROR}/docutils/docutils-${PV}.tar.gz"
SRC_URI[md5sum] = "20ac380a18b369824276864d98ec0ad6"
SRC_URI[sha256sum] = "9af4166adf364447289c5c697bb83c52f1d6f57e77849abcccd6a4a18a5e7ec9"

S = "${WORKDIR}/docutils-${PV}"

inherit distutils

BBCLASSEXTEND = "native"

