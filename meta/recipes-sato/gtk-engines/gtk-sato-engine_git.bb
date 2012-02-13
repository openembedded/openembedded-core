require gtk-sato-engine.inc

LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/sato-utils.h;endline=24;md5=708f28cfe7fe028d497aaf4389b80b62 \
                    file://src/sato-main.c;endline=24;md5=b5e5dddebca570275becb51b526e4c5a"

SRCREV = "e4a29fbb7648f12fb3aefd8ea0120c3f54ba392a"
PV = "0.3.3+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"

S = "${WORKDIR}/git"

EXTRA_OECONF += "${@base_contains('MACHINE_FEATURES', 'qvga', '--with-mode=qvga', '',d)}"
