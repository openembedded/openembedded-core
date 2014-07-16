require oprofile.inc

DEPENDS += "virtual/kernel"
DEPENDS_append_powerpc64 = " libpfm4"

SRC_URI += "${SOURCEFORGE_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz \
            file://0001-Add-rmb-definition-for-AArch64-architecture.patch \
            file://0001-Tidy-powerpc64-bfd-target-check.patch \
            file://0001-Add-freescale-e500mc-support.patch \
            file://0002-Add-freescale-e6500-support.patch \
           "
SRC_URI[md5sum] = "00aec1287da2dfffda17a9b1c0a01868"
SRC_URI[sha256sum] = "1e523400daaba7b8d0d15269e977a08b40edfea53970774b69ae130e25117597"


S = "${WORKDIR}/oprofile-${PV}"

