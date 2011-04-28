require gtk-sato-engine.inc

LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/sato-utils.h;endline=24;md5=708f28cfe7fe028d497aaf4389b80b62 \
                    file://src/sato-main.c;endline=24;md5=b5e5dddebca570275becb51b526e4c5a"

SRCREV = "163"
PV = "0.3.2+svnr${SRCPV}"
PR = "r0"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=gtk-engine;proto=http"

EXTRA_OECONF += "${@base_contains('MACHINE_FEATURES', 'qvga', '--with-mode=qvga', '',d)}"

S = "${WORKDIR}/gtk-engine"
