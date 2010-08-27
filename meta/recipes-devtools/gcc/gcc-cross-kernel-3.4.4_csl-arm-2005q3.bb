# This kernel compiler is required by the Freecom FSG-3 machine
# This kernel compiler is required by the Nokia tablets
# Please talk to Rod Whitby and Richard Purdie before considering removing this file.

require gcc-csl-arm-2005q3.inc
require gcc-cross.inc
require gcc-cross-initial.inc
require gcc-cross-kernel.inc

DEFAULT_PREFERENCE = "-1"

PR = "r1"

SRC_URI += "file://gcc-3.4.4-makefile-fix.patch;patch=1"

