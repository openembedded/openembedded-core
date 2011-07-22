require conf/machine/include/powerpc/arch-powerpc.inc

TUNE_CCARGS = "-mcpu=e300c2 -msoft-float"
TUNE_PKGARCH = "ppce300"
PACKAGE_EXTRA_ARCHS = "powerpc ppce300"
