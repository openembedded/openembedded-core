DESCRIPTION = "Python HTTP for Humans."
HOMEPAGE = "http://python-requests.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRC_URI[sha256sum] = "d568723a7ebd25875d8d1eaf5dfa068cd2fc8194b2e483d7b1f7c81918dbec6b"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-email \
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-ndg-httpsclient \
    ${PYTHON_PN}-netserver \
    ${PYTHON_PN}-pyasn1 \
    ${PYTHON_PN}-pyopenssl \
    ${PYTHON_PN}-pysocks \
    ${PYTHON_PN}-urllib3 \
    ${PYTHON_PN}-chardet \
    ${PYTHON_PN}-idna \
"

CVE_PRODUCT = "requests"

BBCLASSEXTEND = "native nativesdk"
