DESCRIPTION = "parser for wbxml"
LICENSE = "GPLv2"

DEPENDS = "libxml2 sed-native expat"

SRC_URI = "${SOURCEFORGE_MIRROR}/wbxmllib/${P}.tar.gz \
	   file://no-doc-install.patch;patch=1"

inherit autotools_stage pkgconfig

do_configure_append() {
	sed -i s:-I/usr/include::g Makefile
	sed -i s:-I/usr/include::g */Makefile
}

PACKAGES += "${PN}-tools"

FILES_${PN}-tools = "${bindir}"
FILES_${PN} = "${libdir}/*.so.*"

