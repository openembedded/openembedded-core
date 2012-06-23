DESCRIPTION = "A pro-active password checker library"
HOMEPAGE = "http://sourceforge.net/projects/cracklib"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"

DEPENDS = "zlib"
PR ="r0"

EXTRA_OECONF = "--without-python"

SRC_URI = "${SOURCEFORGE_MIRROR}/cracklib/cracklib-${PV}.tar.gz"

SRC_URI[md5sum] = "ca0ec168d9c6466612204e8dfb2df8a9"
SRC_URI[sha256sum] = "7086b0ca23f875c9cd9ea2a993c262384b274dba9c4ea1da845ec8ac290748a9"

inherit autotools gettext
