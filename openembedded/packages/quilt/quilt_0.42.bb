include quilt.inc

SRC_URI = "cvs://anonymous@cvs.savannah.nongnu.org/cvsroot/quilt;method=pserver;module=quilt;tag=VER_${@(bb.data.getVar('PV', d, 1) or '').replace('.', '_')} \
	   file://install.patch;patch=1 \
	   file://nostrip.patch;patch=1"
S = "${WORKDIR}/quilt"

inherit autotools gettext

include quilt-package.inc
