include quilt.inc

PR = "r2"
SRC_URI = "cvs://anoncvs:@savannah.nongnu.org/cvsroot/quilt;method=ext;rsh=ssh;module=quilt;tag=VER_${@(bb.data.getVar('PV', d, 1) or '').replace('.', '_')} \
	   file://install.patch;patch=1 \
	   file://nostrip.patch;patch=1"
S = "${WORKDIR}/quilt"

inherit autotools gettext

include quilt-package.inc
