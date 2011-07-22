require conf/machine/include/arm/arch-arm.inc


# Instead of using -mfpu=vfp[2] we can use -mfpu=neon to make use of gcc intrinsics[1] and vectorize loops with -ftree-vectorize[3]
# [1] http://gcc.gnu.org/onlinedocs/gcc/ARM-NEON-Intrinsics.html
# [2] http://gcc.gnu.org/onlinedocs/gcc/ARM-Options.html 
# [3] https://support.codesourcery.com/GNUToolchain/kbentry29

TUNE_CCARGS = "-march=armv7-a -mtune=cortex-a8 -mfpu=neon -mfloat-abi=softfp -fno-tree-vectorize"

# Other potentially useful options
#-ftree-vectorize -ffast-math -fno-omit-frame-pointer

TUNE_PKGARCH = "armv7a"
PACKAGE_EXTRA_ARCHS = "arm armv4 armv4t armv5te armv6 armv7 armv7a"
