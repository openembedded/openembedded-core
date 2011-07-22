require conf/machine/include/powerpc/arch-powerpc.inc

TUNE_CCARGS = "-mcpu=603e  -mhard-float"
TUNE_PKGARCH = "ppc603e"
PACKAGE_EXTRA_ARCHS = "powerpc ppc603e"
