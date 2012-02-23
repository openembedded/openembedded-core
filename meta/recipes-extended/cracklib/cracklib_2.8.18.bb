DESCRIPTION = "A pro-active password checker library"
HOMEPAGE = "http://sourceforge.net/projects/cracklib"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"

DEPENDS = "zlib"
PR ="r4"

EXTRA_OECONF = "--without-python"

SRC_URI = "${SOURCEFORGE_MIRROR}/cracklib/cracklib-${PV}.tar.gz"

SRC_URI[md5sum] = "79053ad8bc714a44cd660cb12116211b"
SRC_URI[sha256sum] = "2b072f67f7267358459424c3ed70f7f5b70919118e6504a90d3bce37a67c1454"

inherit autotools gettext
