# valid options for -march: `armv7', `armv7-a', `armv7-r', `armv7-m'
# valid option for -mtune: `cortex-a8', `cortex-r4', `cortex-m3', `cortex-m1'
# This will NOT compile programs in 'ARM' mode, which is what you really want
TARGET_CC_ARCH = "-march=armv7 -mfpu=vfp -mfloat-abi=softfp"
FEED_ARCH = "armv7"
PACKAGE_ARCH = "armv7"
