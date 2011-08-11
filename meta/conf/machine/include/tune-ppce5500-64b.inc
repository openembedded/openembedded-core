DEFAULTTUNE ?= "ppc64e5500"

require conf/machine/include/powerpc/arch-powerpc64.inc

TUNEVALID[ppc64e5500] = "Enable ppc64e5500 specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "ppc64e5500", "-mcpu=e5500", "", d)}"
TUNE_PKGARCH = "${@bb.utils.contains("TUNE_FEATURES", "ppc64e5500", "ppc64e5500", "${PPCPKGARCH}", d)}"

AVAILTUNES = "ppc64e5500"
TUNE_FEATURES_tune-ppc64e5500 = "m64 ppc64e5500"
PACKAGE_EXTRA_ARCHS_tune-ppc64e5500 = "powerpc64 ppc64e5500"
BASE_LIB_tune-ppc64e5500 = "lib64"

# glibc configure options to get e5500 specific library (for sqrt)
GLIBC_EXTRA_OECONF += "${@bb.utils.contains("TUNE_FEATURES", "ppc64e5500", "--with-cpu=e5500", "", d)}"
