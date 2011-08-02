DEFAULTTUNE ?= "ppce500v2"

require conf/machine/include/powerpc/arch-powerpc.inc

TUNEVALID[ppce500v2] = "Enable ppce500v2 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "ppce500v2", "-mcpu=8548", "", d)}"
TUNE_PKGARCH = "${@bb.utils.contains("TUNE_FEATURES", "ppce500v2", "ppce500v2", "${PPCPKGARCH}", d)}"

TUNEVALID[spe] = "Enable SPE ABI extensions"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "spe", "-mabi=spe -mspe", "", d)}"

TARGET_FPU = "ppc-efd"

AVAILTUNES += "ppce500v2"
TUNE_FEATURES_tune-ppce500v2 = "m32 spe ppce500v2"
PACKAGE_EXTRA_ARCHS_tune-ppce500v2 = "ppce500v2"
