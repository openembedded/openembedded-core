SUMMARY = "Timezone data"
HOMEPAGE = "http://www.iana.org/time-zones"
SECTION = "base"
LICENSE = "PD & BSD & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c679c9d6b02bc2757b3eaf8f53c43fba"

PV = "2018i"

SRC_URI =" http://www.iana.org/time-zones/repository/releases/tzcode${PV}.tar.gz;name=tzcode \
           http://www.iana.org/time-zones/repository/releases/tzdata${PV}.tar.gz;name=tzdata \
           "

UPSTREAM_CHECK_URI = "http://www.iana.org/time-zones"

SRC_URI[tzcode.md5sum] = "6a6d98be8fa2fa3485e25343e79188b4"
SRC_URI[tzcode.sha256sum] = "aaacdb876ca6fb9d58e244b462cbc7578a496b1b10994381b4b32b9f2ded32dc"
SRC_URI[tzdata.md5sum] = "b3f0a1a789480a036e58466cd0702477"
SRC_URI[tzdata.sha256sum] = "82c45ef84ca3bc01d0a4a397ba8adeb8f7f199c6550740587c6ac5a7108c00d9"

