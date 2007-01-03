DESCRIPTION = "parser for wbxml"
LICENSE = "GPLv2"

DEPENDS = "libxml2 sed-native"

SRC_URI = "${SOURCEFORGE_MIRROR}/wbxmllib/${P}.tar.gz \
	   file://no-doc-install.patch;patch=1"

inherit autotools pkgconfig

do_configure_append() {
	sed -i s:-I/usr/include::g Makefile
	sed -i s:-I/usr/include::g */Makefile
}

do_stage() {
	autotools_stage_all
}

PACKAGES += "${PN}-tools"

FILES_${PN}-tools = "${bindir}"
FILES_${PN} = "${libdir}/*.so.*"

