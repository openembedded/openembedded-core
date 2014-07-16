SUMMARY = "Software Construction tool (make/autotools replacement)"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f0ad8a74a10870caa5e08a3e45d719e2"
SRCNAME = "scons"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"

SRC_URI[md5sum] = "bbd428da35ec176575a2b0be1f8d7162"
SRC_URI[sha256sum] = "f993320c5e9515e6567fcce73df0a7a8808414bf1223c69123d3dcf339a449d4"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
