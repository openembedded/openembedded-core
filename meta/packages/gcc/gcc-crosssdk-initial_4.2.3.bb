require gcc-cross-initial_${PV}.bb
inherit crosssdk

SYSTEMHEADERS = "${SDKPATH}/include"
SYSTEMLIBS1 = "${SDKPATH}/lib/"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-initial-crosssdk"
