SRC_URI = "git://www.jdl.com/software/dtc.git;protocol=git \
	file://fix_for_compilation_with_gcc_4.6.0.patch"

SRCREV = "73dca9ae0b9abe6924ba640164ecce9f8df69c5a"
PV = "1.2.0+git${SRCPV}"
PR = "r3"

S = "${WORKDIR}/git"
