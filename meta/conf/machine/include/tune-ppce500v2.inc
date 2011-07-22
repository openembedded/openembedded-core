require conf/machine/include/powerpc/arch-powerpc.inc

TUNE_CCARGS = "-mcpu=8548 -mabi=spe -mspe"
TUNE_PKGARCH = "ppce500v2"
PACKAGE_EXTRA_ARCHS = "powerpc ppce500v2"
