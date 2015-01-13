require oprofile.inc

DEPENDS += "virtual/kernel"
DEPENDS_append_powerpc64 = " libpfm4"

SRC_URI += "${SOURCEFORGE_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "ba0b340e5c421a93959776c836ed35b3"
SRC_URI[sha256sum] = "847110b4ecdcf8c8353cd38f94c1b704aad4bfcd9453e38b88d112cfb7e3c45a"

S = "${WORKDIR}/oprofile-${PV}"

PR = "r1"
