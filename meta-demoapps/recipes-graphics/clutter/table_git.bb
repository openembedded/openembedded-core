require table.inc

LIC_FILES_CHKSUM = "file://fluttr/COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://script-viewer/COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

SRCREV = "4b267533ce16656cba4104fc39dc12709c1bdddf"
PV = "0.3.0+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/toys.git;protocol=git \
           file://fixes.patch;patch=1"

S = "${WORKDIR}/git/table"


