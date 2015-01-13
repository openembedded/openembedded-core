SUMMARY = "Password strength checker library"
HOMEPAGE = "http://sourceforge.net/projects/cracklib"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"

DEPENDS = "cracklib-native zlib"
DEPENDS_class-native = "zlib-native"

EXTRA_OECONF = "--without-python --libdir=${base_libdir}"

SRC_URI = "${SOURCEFORGE_MIRROR}/cracklib/cracklib-${PV}.tar.gz \
           file://0001-packlib.c-support-dictionary-byte-order-dependent.patch \
           file://0002-craklib-fix-testnum-and-teststr-failed.patch"

SRC_URI[md5sum] = "559072fdfc095cdb763c4de3471a889e"
SRC_URI[sha256sum] = "c1c899291d443e99d1aecfbc879e4ac9c0cbc265574f47b487842da11e9759f5"

PR = "r1"

inherit autotools gettext

do_install_append_class-target() {
	create-cracklib-dict -o ${D}${datadir}/cracklib/pw_dict ${D}${datadir}/cracklib/cracklib-small
}

BBCLASSEXTEND = "native nativesdk"
