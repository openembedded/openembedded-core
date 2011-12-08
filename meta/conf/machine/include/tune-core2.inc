DEFAULTTUNE ?= "core2"
TUNE_PKGARCH ?= "${@bb.utils.contains("TUNE_FEATURES", "m32", "core2", "core2_64", d)}"

require conf/machine/include/tune-i586.inc

# Extra tune features
TUNEVALID[core2] = "Enable core2 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "core2", "-march=core2 -msse3 -mtune=generic -mfpmath=sse", "", d)}"

# Extra tune selections
AVAILTUNES += "core2"
TUNE_FEATURES_tune-core2 ?= "${TUNE_FEATURES_tune-x86} core2"
BASE_LIB_tune-core2 ?= "lib"
PACKAGE_EXTRA_ARCHS_tune-core2 = "${PACKAGE_EXTRA_ARCHS_tune-x86} i386 i486 i586 i686 core2"

AVAILTUNES += "core2-64"
TUNE_FEATURES_tune-core2-64 ?= "${TUNE_FEATURES_tune-x86-64} core2"
BASE_LIB_tune-core2-64 ?= "lib64"
PACKAGE_EXTRA_ARCHS_tune-core2-64 = "${PACKAGE_EXTRA_ARCHS_tune-x86-64} core2_64"

AVAILTUNES += "core2-64-x32"
TUNE_FEATURES_tune-core2-64-x32 ?= "${TUNE_FEATURES_tune-x86-64-x32} core2"
BASE_LIB_tune-core2-64-x32 ?= "libx32"
PACKAGE_EXTRA_ARCHS_tune-core2-64-x32 = "${PACKAGE_EXTRA_ARCHS_tune-x86-64-x32} core2_64_x32"
