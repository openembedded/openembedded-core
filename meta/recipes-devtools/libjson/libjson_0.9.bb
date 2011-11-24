DESCRIPTION = "JSON-C - A JSON implementation in C"
HOMEPAGE = "http://oss.metaparadigm.com/json-c/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=30a276a476b02c2dcd0849bde417fb17"

SRC_URI = "http://oss.metaparadigm.com/json-c/json-c-${PV}.tar.gz"
SRC_URI[md5sum] = "3a13d264528dcbaf3931b0cede24abae"
SRC_URI[sha256sum] = "702a486c9bf8e19137d484ab5c49b4ad314eb5e1fe37062a72c0a0fa39439475"

S = "${WORKDIR}/json-c-${PV}"


inherit autotools
