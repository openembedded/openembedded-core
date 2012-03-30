DEFAULTTUNE ?= "cortexr4"
ARMPKGARCH ?= "armv7r"

require conf/machine/include/arm/arch-armv7a.inc

TUNEVALID[cortexr4] = "Enable Cortex-R4 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "cortexr4", "-mtune=cortex-r4", "", d)}"

TUNEVALID[armv7r] = "Enable Cortex-R4 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "armv7r", "-march=armv7-m", "", d)}"

AVAILTUNES += "cortexr4"
TUNE_FEATURES_tune-cortexr4 = "armv7r vfp cortexr4"
PACKAGE_EXTRA_ARCHS_tune-cortexr4 = "${PACKAGE_EXTRA_ARCHS_tune-armv7at} armv7r-vfp"
