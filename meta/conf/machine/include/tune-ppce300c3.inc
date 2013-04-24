DEFAULTTUNE ?= "ppce300c3"

require conf/machine/include/powerpc/arch-powerpc.inc

TUNEVALID[ppce300c3] = "Enable ppce300c3 specific processor optimizations"
TUNE_CCARGS .= "${@bb.utils.contains("TUNE_FEATURES", "ppce300c3", " -mcpu=e300c3", "", d)}"

AVAILTUNES += "ppce300c3"
TUNE_FEATURES_tune-ppce300c3 = "m32 fpu-soft ppce300c3"
TUNE_PKGARCH_tune-ppce300c3 = "ppce300c3"
PACKAGE_EXTRA_ARCHS_tune-ppce300c3 = "${PACKAGE_EXTRA_ARCHS_tune-powerpc-nf} ppce300c3"
