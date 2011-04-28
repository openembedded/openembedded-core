require tidy.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://tidy/tidy.h;endline=21;md5=ac342e7aac55849bf0755d79967fa3e8"

SRCREV = "e25416e1293e1074bfa6727c80527dcff5b1f3cb"
PV = "0.1.0+git${SRCPV}"
PR = "r7"

SRC_URI = "git://git.clutter-project.org/tidy.git;protocol=git \
           file://tidy-enable-tests.patch;patch=1"

S = "${WORKDIR}/git"

