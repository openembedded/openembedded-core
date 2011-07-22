require conf/machine/include/arm/arch-arm.inc

TUNE_PKGARCH = "armv5te"
PACKAGE_EXTRA_ARCHS = "arm armv4 armv4t armv5te"
# For gcc 3.x you need:
#TUNE_CCARGS = "-march=armv5te -mtune=arm926ejs"
# For gcc 4.x you need:
TUNE_CCARGS = "-march=armv5te -mtune=arm926ej-s"

