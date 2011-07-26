DEFAULTTUNE ?= "ppc603e"

require conf/machine/include/powerpc/arch-powerpc.inc

TUNEVALID[ppc603e] = "Enable ppc603e specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "ppc603e", "-mcpu=603e", "", d)}"
TUNE_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ppc603e", "ppc603e", "", d)}"

AVAILTUNES += "ppc603e"
TUNE_FEATURES_tune-ppc603e = "m32 fpu-hard ppc603e"
PACKAGE_EXTRA_ARCHS_tune-ppc603e = "powerpc ppc603e"
