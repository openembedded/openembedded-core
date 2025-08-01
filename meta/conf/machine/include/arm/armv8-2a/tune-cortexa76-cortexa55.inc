#
# Tune Settings for big.LITTLE Cortex-A76 - Cortex-A55
#
DEFAULTTUNE                                        ?= "cortexa76-cortexa55"

TUNEVALID[cortexa76-cortexa55] = "Enable big.LITTLE Cortex-A76.Cortex-A55 specific processor optimizations"
MACHINEOVERRIDES =. "${@bb.utils.contains("TUNE_FEATURES", "cortexa76-cortexa55", "cortexa76-cortexa55:", "", d)}"

CPU_TUNE_ARG = "cortex-a76.cortex-a55"
# clang doesn't support big.LITTLE tunes, select the LITTLE tune
CPU_TUNE_ARG:toolchain-clang = "cortex-a55"
TUNE_CCARGS .= "${@bb.utils.contains("TUNE_FEATURES", "cortexa76-cortexa55", " -mcpu=${CPU_TUNE_ARG}", "", d)}"

require conf/machine/include/arm/arch-armv8-2a.inc

AVAILTUNES                                         += "cortexa76-cortexa55 cortexa76-cortexa55-crypto"
ARMPKGARCH:tune-cortexa76-cortexa55                 = "cortexa76-cortexa55"
ARMPKGARCH:tune-cortexa76-cortexa55-crypto          = "cortexa76-cortexa55-crypto"
# We do not want -march since -mcpu is added above to cover for it
TUNE_FEATURES:tune-cortexa76-cortexa55              = "aarch64 cortexa76-cortexa55"
TUNE_FEATURES:tune-cortexa76-cortexa55-crypto       = "${TUNE_FEATURES:tune-cortexa76-cortexa55} crypto"
PACKAGE_EXTRA_ARCHS:tune-cortexa76-cortexa55        = "${PACKAGE_EXTRA_ARCHS:tune-armv8-2a} cortexa76-cortexa55"
PACKAGE_EXTRA_ARCHS:tune-cortexa76-cortexa55-crypto = "${PACKAGE_EXTRA_ARCHS:tune-armv8-2a-crypto} cortexa76-cortexa55 cortexa76-cortexa55-crypto"
BASE_LIB:tune-cortexa76-cortexa55                   = "lib64"
BASE_LIB:tune-cortexa76-cortexa55-crypto            = "lib64"
