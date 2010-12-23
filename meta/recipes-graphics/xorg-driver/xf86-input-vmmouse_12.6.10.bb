require xf86-input-common.inc

DESCRIPTION = "X.Org X server -- VMWare mouse input driver"
PR = "r0"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=622841c068a9d7625fbfe7acffb1a8fc"

SRC_URI[md5sum] = "49c6e77851e9f7bc5cb7d85f061992f8"
SRC_URI[sha256sum] = "a6369d5a860627f2a38842d5563045b263a459e534f6ae08df48f330f9a40910"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'
