DEFAULTTUNE ?= "ppce500"

require conf/machine/include/powerpc/arch-powerpc.inc

TUNEVALID[ppce500] = "Enable ppce500 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "ppce500", "-mcpu=8540", "", d)}"
TUNE_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ppce500", "ppce500", "", d)}"

TUNEVALID[spe] = "Enable SPE ABI extensions"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "spe", "-mabi=spe -mspe", "", d)}"

TARGET_FPU = "ppc-efs"

AVAILTUNES += "ppce500"
TUNE_FEATURES_tune-ppce500 = "m32 spe ppce500"
PACKAGE_EXTRA_ARCHS_tune-ppce500 = "ppce500"
