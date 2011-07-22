require conf/machine/include/arm/arch-arm.inc

# valid options for -march: `armv7', `armv7-a', `armv7-r', `armv7-m'
# valid option for -mtune: `cortex-a8', `cortex-r4', `cortex-m3', `cortex-m1'
# This will NOT compile programs in 'ARM' mode, which is what you really want
TUNE_CCARGS = "-march=armv7 -mfpu=vfp -mfloat-abi=softfp"
TUNE_PKGARCH = "armv7"
PACKAGE_EXTRA_ARCHS = "arm armv4 armv4t armv5te armv6 armv7"
