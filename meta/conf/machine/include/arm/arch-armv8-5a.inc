DEFAULTTUNE ?= "armv8-5a"

TUNEVALID[armv8-5a] = "Enable instructions for ARMv8.5-a"
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'armv8-5a', ' -march=armv8.5-a', '', d)}"
# TUNE crypto will be handled by arch-armv8a.inc below
MACHINEOVERRIDES =. "${@bb.utils.contains('TUNE_FEATURES', 'armv8-5a', 'armv8-5a:', '', d)}"

require conf/machine/include/arm/arch-armv8a.inc

# Little Endian base configs
AVAILTUNES += "armv8-5a armv8-5a-crypto"
ARMPKGARCH_tune-armv8-5a                    ?= "armv8-5a"
ARMPKGARCH_tune-armv8-5a-crypto             ?= "armv8-5a"
TUNE_FEATURES_tune-armv8-5a                  = "aarch64 armv8-5a"
TUNE_FEATURES_tune-armv8-5a-crypto           = "${TUNE_FEATURES_tune-armv8-5a} crypto"
PACKAGE_EXTRA_ARCHS_tune-armv8-5a            = "${PACKAGE_EXTRA_ARCHS_tune-armv8a} armv8-5a"
PACKAGE_EXTRA_ARCHS_tune-armv8-5a-crypto     = "${PACKAGE_EXTRA_ARCHS_tune-armv8-5a} armv8-5a-crypto"
BASE_LIB_tune-armv8-5a                       = "lib64"
BASE_LIB_tune-armv8-5a-crypto                = "lib64"
