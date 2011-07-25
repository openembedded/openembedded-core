DEFAULTTUNE ?= "ep9312"
ARMPKGARCH ?= "ep9312"

require conf/machine/include/arm/arch-armv4.inc

TUNEVALID[ep9312] = "Enable Intel PXA27x specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "ep9312", "-march=ep9312 -mtune=ep9312", "", d)}"

AVAILTUNES += "ep9312"
TUNE_FEATURES_tune-ep9312 = "thumb ep9312"
PACKAGE_EXTRA_ARCHS_tune-ep9312 = "${PACKAGE_EXTRA_ARCHS_tune-armv4t} ep9312"


