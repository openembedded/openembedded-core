SECTION = "console/utils"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/desktop-file-utils"
DESCRIPTION = "command line utilities to work with *.desktop files"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS = "glib-2.0-native"

SRC_URI = "http://freedesktop.org/software/desktop-file-utils/releases/desktop-file-utils-${PV}.tar.xz"

SRC_URI[md5sum] = "fda5c312c9fb3b8d818fb54f2c34db82"
SRC_URI[sha256sum] = "b6c9b860538ef1cffbcdfbc9cb578f85a080ad8c1207c8b3a39e9fd183f9782b"

inherit autotools native

S = "${WORKDIR}/desktop-file-utils-${PV}"
