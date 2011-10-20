require oprofile.inc

PR = "${INC_PR}.0"

SRC_URI += "${SOURCEFORGE_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "8b5d1d9b65f84420bcc3234777ad3be3"
SRC_URI[sha256sum] = "cb5b507d5dea058d223fcad3cec8ff9638a4163106afd66d176798bbd973d527"

S = "${WORKDIR}/oprofile-${PV}"

