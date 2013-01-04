require oprofile.inc

PR = "${INC_PR}.0"

SRC_URI += "${SOURCEFORGE_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "6d127023af1dd1cf24e15411229f3cc8"
SRC_URI[sha256sum] = "ab45900fa1a23e5d5badf3c0a55f26c17efe6e184efcf00b371433751fa761bc"

S = "${WORKDIR}/oprofile-${PV}"

