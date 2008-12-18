require clutter.inc

PV = "0.8.0+gitr${SRCREV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=clutter-0-8 \
	   file://enable_tests-0.8.patch;patch=1 "

S = "${WORKDIR}/git"
