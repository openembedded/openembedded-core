SUMMARY = "Parsing and Encoding of WBXML documents"
DESCRIPTION = "The WBXML Library (aka libwbxml) contains a library and \
its associated tools to Parse, Encode and Handle WBXML documents. The \
WBXML format is a binary representation of XML, defined by the Wap \
Forum, and used to reduce bandwidth in mobile communications."
LICENSE = "GPLv2"

DEPENDS = "libxml2 sed-native expat"

SRC_URI = "${SOURCEFORGE_MIRROR}/wbxmllib/${BPN}-${PV}.tar.gz \
	   file://no-doc-install.patch;patch=1"

inherit autotools pkgconfig

do_configure_append() {
	sed -i s:-I/usr/include::g Makefile
	sed -i s:-I/usr/include::g */Makefile
}

PACKAGES += "${PN}-tools"

FILES_${PN}-tools = "${bindir}"
FILES_${PN} = "${libdir}/*.so.*"

