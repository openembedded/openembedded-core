DESCRIPTION = "shared MIME database and spec"
HOMEPAGE = "http://freedesktop.org/wiki/Software/shared-mime-info"
SECTION = "base"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

DEPENDS = "libxml2 intltool-native glib-2.0 shared-mime-info-native"
DEPENDS_virtclass-native = "libxml2-native intltool-native glib-2.0-native"

SRC_URI = "http://freedesktop.org/~hadess/shared-mime-info-${PV}.tar.bz2"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-update-mimedb"

FILES_${PN} += "${datadir}/mime"
FILES_${PN}-dev += "${datadir}/pkgconfig/shared-mime-info.pc"

do_install () {
	autotools_do_install

	update-mime-database ${D}${datadir}/mime

	# we do not need it on device and it is huge
	rm ${D}${datadir}/mime/packages/freedesktop.org.xml
}

do_install_virtclass-native () {
	autotools_do_install
}

BBCLASSEXTEND = "native"
