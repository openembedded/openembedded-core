# This kernel compiler is required by the Freecom FSG-3 machine
# Please talk to Rod Whitby before considering removing this file.

DEFAULT_PREFERENCE = "-1"

require gcc-cross-kernel.inc
require gcc-cross-initial_csl-arm-2005q3.bb

SRC_URI += "file://gcc-3.4.4-makefile-fix.patch;patch=1"

