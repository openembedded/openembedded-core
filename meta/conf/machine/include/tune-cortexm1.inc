require conf/machine/include/arm/arch-arm.inc

TUNE_CCARGS = "-march=armv7 -mtune=cortex-m1 -mfpu=vfp -mfloat-abi=softfp"
TUNE_PKGARCH = "armv6"
