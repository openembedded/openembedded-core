require conf/machine/include/powerpc/arch-powerpc.inc

TUNE_CCARGS = "-mcpu=e500mc"
TUNE_PKGARCH = "ppce500mc"
PACKAGE_EXTRA_ARCHS = "powerpc ppce500mc"
