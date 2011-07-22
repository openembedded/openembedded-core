require conf/machine/include/arm/arch-arm.inc

TUNE_PKGARCH = "armv4t"
TUNE_CCARGS = "-march=armv4t -mtune=arm920t"
PACKAGE_EXTRA_ARCHS = "arm armv4 armv4t"
