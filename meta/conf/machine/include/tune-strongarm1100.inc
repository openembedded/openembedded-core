DEFAULTTUNE ?= "strongarm"

require conf/machine/include/arm/arch-armv4.inc

TUNEVALID[strongarm] = "Enable Strongarm 1100 series processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "strongarm", "-mtune=strongarm1100", "", d)}"

AVAILTUNES += "strongarm"
TUNE_FEATURES_tune-strongarm = "${TUNE_FEATURES_tune-armv4} strongarm"
PACKAGE_EXTRA_ARCHS_tune-strongarm = "${PACKAGE_EXTRA_ARCHS_tune-armv4}"


