SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libogg"
DESCRIPTION = "tremor is a fixed point implementation of the vorbis codec."
LICENSE = "BSD"
SRCDATE = "${PV}"
PR = "r2"

SRC_URI = "svn://svn.xiph.org/trunk;module=Tremor;rev=4573;proto=http"

S = "${WORKDIR}/Tremor"

inherit autotools

EXTRA_OECONF=" --enable-shared --disable-rpath  "

#do_configure_prepend() {
#    ./autogen.sh
#}

do_stage() {
	autotools_stage_all
}

ARM_INSTRUCTION_SET = "arm"
