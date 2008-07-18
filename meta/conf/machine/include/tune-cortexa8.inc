# Instead of using -mfpu=vfp[2] we can use -mfpu=neon to make use of gcc intrinsics[1] and vectorize loops with -ftree-vectorize[3]
# [1] http://gcc.gnu.org/onlinedocs/gcc/ARM-NEON-Intrinsics.html
# [2] http://gcc.gnu.org/onlinedocs/gcc/ARM-Options.html 
# [3] https://support.codesourcery.com/GNUToolchain/kbentry29

TARGET_CC_ARCH = "-march=armv7-a -mtune=cortex-a8 -mfpu=neon -mfloat-abi=softfp"
# Override the options for some packages
TARGET_CC_ARCH_pn-liba52 = "-march=armv7-a -mtune=cortex-a8 -mfloat-abi=softfp"
TARGET_CC_ARCH_pn-libmad = "-mfpu=neon -mfloat-abi=softfp"
# Other potentially useful options
#-ftree-vectorize -ffast-math -fno-omit-frame-pointer
FEED_ARCH = "armv7a"
BASE_PACKAGE_ARCH = "armv7a"
