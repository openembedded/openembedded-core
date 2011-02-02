SUMMARY = "Program to create dependencies in makefiles"

DESCRIPTION = "The gccmakedep program calls 'gcc -M' to output makefile \
rules describing the dependencies of each sourcefile, so that make knows \
which object files must be recompiled when a dependency has changed."

require xorg-util-common.inc
LIC_FILES_CHKSUM = "file://Makefile.am;endline=20;md5=23c277396d690413245ebb89b18c5d4d"
DESCRIPTION = "create dependencies in makefiles using 'gcc -M'"
DEPENDS = "util-macros"
RDEPENDS_${PN} = "gcc"

PR = "r3"
PE = "1"

SRC_URI[md5sum] = "fc49f45251c1336fe1dad5dba1c83fcd"
SRC_URI[sha256sum] = "fdd3963294e80b27416f902a5c029c033d321f03310d3cafa3afb62b50ddce92"
