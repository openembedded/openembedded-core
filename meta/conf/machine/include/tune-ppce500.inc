require conf/machine/include/powerpc/arch-powerpc.inc

TUNE_CCARGS = "-mcpu=8540"
BASE_PACKAGE_ARCH = "ppce500"
TUNE_PKGARCH = "ppce500"
PACKAGE_EXTRA_ARCHS = "powerpc ppce500"
