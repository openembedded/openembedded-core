DESCRIPTION = "A pro-active password checker library"
HOMEPAGE = "http://sourceforge.net/projects/cracklib"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"

DEPENDS = "cracklib-native zlib"
DEPENDS_class-native = "zlib-native"

EXTRA_OECONF = "--without-python --libdir=${base_libdir}"

SRC_URI = "${SOURCEFORGE_MIRROR}/cracklib/cracklib-${PV}.tar.gz \
           file://0001-packlib.c-support-dictionary-byte-order-dependent.patch \
           file://0002-craklib-fix-testnum-and-teststr-failed.patch"

SRC_URI[md5sum] = "e0f94ac2138fd33c7e77b19c1e9a9390"
SRC_URI[sha256sum] = "17fecdfa78c0b9b47afa1533ea99b5351c3cef770bbd9c8c34391827efecbdc0"

inherit autotools gettext

BBCLASSEXTEND = "native"

do_install_append_class-target() {
	create-cracklib-dict -o ${D}${datadir}/cracklib/pw_dict ${D}${datadir}/cracklib/cracklib-small
}
