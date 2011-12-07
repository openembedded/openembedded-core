DEFAULTTUNE ?= "armv4"

ARMPKGARCH ?= "armv4"

TUNEVALID[armv4] = "Enable instructions for ARMv4"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "armv4", "-march=armv4${ARMPKGSFX_THUMB}", "", d)}"
MACHINEOVERRIDES .= "${@bb.utils.contains("TUNE_FEATURES", "armv4", ":armv4", "" ,d)}"

require conf/machine/include/arm/arch-arm.inc
require conf/machine/include/arm/feature-arm-thumb.inc

# Little Endian
AVAILTUNES += "armv4 armv4t"
TUNE_FEATURES_tune-armv4 ?= "armv4"
TUNE_FEATURES_tune-armv4t ?= "armv4 thumb"
PACKAGE_EXTRA_ARCHS_tune-armv4 = "arm armv4"
PACKAGE_EXTRA_ARCHS_tune-armv4t = "${PACKAGE_EXTRA_ARCHS_tune-armv4} armv4t"

# Big Endian
AVAILTUNES += "armv4b armv4tb"
TUNE_FEATURES_tune-armv4b ?= "${TUNE_FEATURES_tune-armv4} bigendian"
TUNE_FEATURES_tune-armv4tb ?= "${TUNE_FEATURES_tune-armv4t} bigendian"
PACKAGE_EXTRA_ARCHS_tune-armv4b = "armeb armv4b"
PACKAGE_EXTRA_ARCHS_tune-armv4tb = "${PACKAGE_EXTRA_ARCHS_tune-armv4} armv4tb"

