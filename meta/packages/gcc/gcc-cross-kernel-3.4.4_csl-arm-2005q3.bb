# This kernel compiler is required by the Freecom FSG-3 machine
# This kernel compiler is required by the Nokia tablets
# Please talk to Rod Whitby and Richard Purdie before considering removing this file.

DEFAULT_PREFERENCE = "-1"

require gcc-cross-initial_csl-arm-2005q3.bb
require gcc-cross-kernel.inc

PR = "r1"

SRC_URI += "file://gcc-3.4.4-makefile-fix.patch;patch=1"

