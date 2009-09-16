require gcc-cross-intermediate_${PV}.bb
inherit crosssdk

SYSTEMHEADERS = "${SDKPATH}/include"
SYSTEMLIBS1 = "${SDKPATH}/lib/"

PR = "r1"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk"
DEPENDS += "virtual/${TARGET_PREFIX}libc-initial-nativesdk"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-intermediate-crosssdk"
