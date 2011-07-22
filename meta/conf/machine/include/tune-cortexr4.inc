require conf/machine/include/arm/arch-arm.inc

# valid options for -march: `armv7', `armv7-r'
TUNE_CCARGS = "-march=armv7-r -mtune=cortex-r4 -mfpu=vfp -mfloat-abi=softfp"
TUNE_PKGARCH = "armv7"
