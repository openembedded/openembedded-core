SUMMARY = "Python HTTP library with thread-safe connection pooling, file post support, sanity friendly, and more"
HOMEPAGE = "https://github.com/shazow/urllib3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c2823cb995439c984fd62a973d79815c"

SRC_URI[sha256sum] = "40c2dc0c681e47eb8f90e7e27bf6ff7df2e677421fd46756da1161c39ca70d32"

inherit pypi setuptools3

SRC_URI += " \
    file://CVE-2025-50181.patch \
"

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-certifi \
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-email \
    ${PYTHON_PN}-idna \
    ${PYTHON_PN}-netclient \
    ${PYTHON_PN}-pyopenssl \
    ${PYTHON_PN}-threading \
    ${PYTHON_PN}-logging \
"

CVE_PRODUCT = "urllib3"

BBCLASSEXTEND = "native nativesdk"
