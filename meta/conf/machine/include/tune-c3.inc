require conf/machine/include/ia32/arch-ia32.inc

TUNE_PKGARCH = "i586"

TUNE_CCARGS = "-march=c3 -mtune=c3"
PACKAGE_EXTRA_ARCHS = "i386 i486 i586"
