require gcc-cross_${PV}.bb
inherit crosssdk

SYSTEMHEADERS = "${SDKPATH}/include"
SYSTEMLIBS1 = "${SDKPATH}/lib/"

GCCMULTILIB = "--disable-multilib"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk virtual/${TARGET_PREFIX}libc-for-gcc-nativesdk"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-crosssdk virtual/${TARGET_PREFIX}g++-crosssdk"
