require gcc-cross-initial_${PV}.bb
require gcc-cross-kernel.inc

SRC_URI += "file://gcc-3.4.4-makefile-fix.patch;patch=1"

