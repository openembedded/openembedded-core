SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libogg"
DESCRIPTION = "tremor is a fixed point implementation of the vorbis codec."
LICENSE = "BSD"
SRCDATE = "${PV}"
PR = "r0"

SRC_URI = "svn://svn.xiph.org/trunk;module=Tremor;rev=17157;proto=http"

S = "${WORKDIR}/Tremor"

inherit autotools

EXTRA_OECONF=" --enable-shared --disable-rpath  "

ARM_INSTRUCTION_SET = "arm"
