SUMMARY = "Software Construction tool (make/autotools replacement)"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f0ad8a74a10870caa5e08a3e45d719e2"
SRCNAME = "scons"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"

SRC_URI[md5sum] = "91fbbb67c2c65b03c746601baac4a6a5"
SRC_URI[sha256sum] = "4b57d72066fed3b6ff65a7dd9f73633c9b1c09f87520e9b3aae84b3e4864b441"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
