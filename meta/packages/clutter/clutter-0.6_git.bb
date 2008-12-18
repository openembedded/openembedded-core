require clutter.inc

PV = "0.6.0+gitr${SRCREV}"
PR = "r3"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=clutter-0-6 \
	   file://enable_tests-0.6.patch;patch=1 "

S = "${WORKDIR}/git"

