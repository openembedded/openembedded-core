SUMMARY = "Tool for writing very fast and very flexible scanners"
HOMEPAGE = "http://re2c.sourceforge.net/"
AUTHOR = "Marcus Börger <helly@users.sourceforge.net>"
SECTION = "devel"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=64eca4d8a3b67f9dc7656094731a2c8d"

SRC_URI = "https://github.com/skvadrik/re2c/releases/download/${PV}/${BPN}-${PV}.tar.xz"
SRC_URI[sha256sum] = "6cddbb558dbfd697a729cb4fd3f095524480283b89911ca5221835d8a67ae5e0"
UPSTREAM_CHECK_URI = "https://github.com/skvadrik/re2c/releases"

BBCLASSEXTEND = "native nativesdk"

inherit autotools
