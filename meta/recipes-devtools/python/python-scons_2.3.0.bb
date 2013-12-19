SUMMARY = "Software Construction tool (make/autotools replacement)"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=98e9a1e6029e715255c52032a1eba637"
SRCNAME = "scons"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"

SRC_URI[md5sum] = "083ce5624d6adcbdaf2526623f456ca9"
SRC_URI[sha256sum] = "9442069999cf4b2caa94a5886ab4c2c71de1718ed5e9176c18e2b6dbca463b4b"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
