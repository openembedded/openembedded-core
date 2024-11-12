SUMMARY = "A stream-oriented XML parser library"
DESCRIPTION = "Expat is an XML parser library written in C. It is a stream-oriented parser in which an application registers handlers for things the parser might find in the XML document (like start tags)"
HOMEPAGE = "https://github.com/libexpat/libexpat"
SECTION = "libs"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://COPYING;md5=7b3b078238d0901d3b339289117cb7fb"

VERSION_TAG = "${@d.getVar('PV').replace('.', '_')}"

SRC_URI = "https://github.com/libexpat/libexpat/releases/download/R_${VERSION_TAG}/expat-${PV}.tar.bz2  \
           file://run-ptest \
           file://CVE-2024-28757.patch \
	   file://CVE-2023-52426-001.patch \
	   file://CVE-2023-52426-002.patch \
	   file://CVE-2023-52426-003.patch \
	   file://CVE-2023-52426-004.patch \
	   file://CVE-2023-52426-005.patch \
	   file://CVE-2023-52426-006.patch \
	   file://CVE-2023-52426-007.patch \
	   file://CVE-2023-52426-008.patch \
	   file://CVE-2023-52426-009.patch \
	   file://CVE-2023-52426-010.patch \
	   file://CVE-2023-52426-011.patch \
	   file://CVE-2024-45490-0001.patch \
	   file://CVE-2024-45490-0002.patch \
	   file://CVE-2024-45490-0003.patch \
	   file://CVE-2024-45490-0004.patch \
	   file://CVE-2024-45491.patch \
	   file://CVE-2024-45492.patch \
	   file://CVE-2024-50602-01.patch \
	   file://CVE-2024-50602-02.patch \
           "

UPSTREAM_CHECK_URI = "https://github.com/libexpat/libexpat/releases/"

SRC_URI[sha256sum] = "6f0e6e01f7b30025fa05c85fdad1e5d0ec7fd35d9f61b22f34998de11969ff67"

EXTRA_OECMAKE:class-native += "-DEXPAT_BUILD_DOCS=OFF"

RDEPENDS:${PN}-ptest += "bash"

inherit cmake lib_package ptest

do_install_ptest:class-target() {
	install -m 755 ${B}/tests/* ${D}${PTEST_PATH}
}

BBCLASSEXTEND += "native nativesdk"

CVE_PRODUCT = "expat libexpat"
