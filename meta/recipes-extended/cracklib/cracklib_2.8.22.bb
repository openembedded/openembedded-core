DESCRIPTION = "A pro-active password checker library"
HOMEPAGE = "http://sourceforge.net/projects/cracklib"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"

DEPENDS = "cracklib-native zlib"
DEPENDS_class-native = "zlib"
PR ="r0"

EXTRA_OECONF = "--without-python"

SRC_URI = "${SOURCEFORGE_MIRROR}/cracklib/cracklib-${PV}.tar.gz"

SRC_URI[md5sum] = "463177b5c29c7a598c991e12a4898e06"
SRC_URI[sha256sum] = "feaff49bfb513ec10b2618c00d2f7f60776ba93fcc5fa22dd3479dd9cad9f770"

inherit autotools gettext

BBCLASSEXTEND = "native"

do_install_append_class-target() {
	create-cracklib-dict -o ${D}${datadir}/cracklib/pw_dict ${D}${datadir}/cracklib/cracklib-small
}
