inherit crosssdk

SYSTEMHEADERS = "${SDKPATH}/include"
SYSTEMLIBS1 = "${SDKPATH}/lib/"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk gettext-native"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-initial-crosssdk"
