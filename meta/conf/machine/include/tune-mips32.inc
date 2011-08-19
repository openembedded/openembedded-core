DEFAULTTUNE ?= "mips32"

require conf/machine/include/mips/arch-mips.inc

TUNEVALID[mips32] = "Enable mips32 specific processor optimizations"
TUNE_CONFLICTS[mips32] = "n64 n32"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "mips32", "-march=mips32", "", d)}"

AVAILTUNES += "mips32 mips32el"
TUNE_FEATURES_tune-mips32 = "${TUNE_FEATURES_tune-mips} mips32"
PACKAGE_EXTRA_ARCHS_tune-mips32 = "mips"
TUNE_FEATURES_tune-mips32el = "${TUNE_FEATURES_tune-mipsel} mips32"
PACKAGE_EXTRA_ARCHS_tune-mips32el = "mipsel"

