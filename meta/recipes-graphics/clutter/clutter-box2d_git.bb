require clutter-box2d.inc

LIC_FILES_CHKSUM = "file://box2d/License.txt;md5=e5d39ad91f7dc4692dcdb1d85139ec6b"

SRCREV = "92b44f79a8667a31b46b861236392bdf32b7d6c2"
PV = "0.10.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.clutter-project.org/clutter-box2d.git;protocol=git"

S = "${WORKDIR}/git"

DEPENDS += "clutter-1.4"
