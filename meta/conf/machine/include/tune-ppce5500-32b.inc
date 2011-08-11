DEFAULTTUNE ?= "ppce5500"

require conf/machine/include/powerpc/arch-powerpc64.inc

TUNEVALID[ppce5500] = "Enable ppce5500 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "ppce5500", "-mcpu=e5500", "", d)}"
TUNE_PKGARCH = "${@bb.utils.contains("TUNE_FEATURES", "ppce5500", "ppce5500", "${PPCPKGARCH}", d)}"

AVAILTUNES = "ppce5500"
TUNE_FEATURES_tune-ppce5500 = "m32 ppce5500"
PACKAGE_EXTRA_ARCHS_tune-ppce5500 = "powerpc ppce5500"
BASE_LIB_tune-ppce5500 = "lib"

# glibc configure options to get e5500 specific library (for sqrt)
GLIBC_EXTRA_OECONF += "${@bb.utils.contains("TUNE_FEATURES", "ppce5500", "--with-cpu=e5500", "", d)}"
