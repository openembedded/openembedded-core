DESCRIPTION = "A Software Construction Tool"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=61e6307bb15ee490a9ec01fd27022d7e"
SRCNAME = "scons"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"

SRC_URI[md5sum] = "f737f474a02d08156c821bd2d4d4b632"
SRC_URI[sha256sum] = "f7fcd11a7af7ec13d587a2f7cf14a92e3223eea64bf078454a9856ec0ab465b9"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
