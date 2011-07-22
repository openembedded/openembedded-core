require conf/machine/include/arm/arch-arm.inc

TUNE_PKGARCH = "armv4t"
PACKAGE_EXTRA_ARCHS = "arm armv4 armv4t"
TUNE_CCARGS = "-mcpu=arm9tdmi -mtune=arm9tdmi"
