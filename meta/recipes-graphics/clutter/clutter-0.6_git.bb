require clutter.inc

PV = "0.6.0+gitr${SRCPV}"
PR = "r5"

PACKAGES =+ "clutter-examples-0.6"
FILES_clutter-examples-0.6 = "${bindir}/test-* ${pkgdatadir}/redhand.png"


SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=clutter-0-6 \
           file://symconflict.patch;patch=1 \
	   file://enable_tests-0.6.patch;patch=1 "

S = "${WORKDIR}/git"

