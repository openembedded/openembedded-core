TUNE_ARCH = "arm"

TUNE_CCARGS = "-march=ep9312 -mtune=ep9312 -mcpu=ep9312"
# add "-mfp=maverick" for newer gcc versions > 4.0

#set arch to ep9312 for all generated packages
PACKAGE_EXTRA_ARCHS = "arm armv4t ep9312"
TUNE_PKGARCH = "ep9312"

