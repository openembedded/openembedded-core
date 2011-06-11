require clutter-box2d.inc

LIC_FILES_CHKSUM = "file://box2d/License.txt;md5=e5d39ad91f7dc4692dcdb1d85139ec6b"

SRCREV = "4799ac10ae8cb7da936a2b999aba58fe62eb1ee3"
PV = "0.10.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.gnome.org/clutter-box2d.git"

S = "${WORKDIR}/git"

DEPENDS += "clutter-1.6"
