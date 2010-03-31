inherit crosssdk

SYSTEMHEADERS = "${SDKPATH}/include"
SYSTEMLIBS1 = "${SDKPATH}/lib/"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk gettext-native"
DEPENDS += "virtual/${TARGET_PREFIX}libc-initial-nativesdk"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-intermediate-crosssdk"
