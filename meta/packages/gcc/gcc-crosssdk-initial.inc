inherit crosssdk

SYSTEMHEADERS = "${SDKPATHNATIVE}${prefix_nativesdk}/include"
SYSTEMLIBS1 = "${SDKPATHNATIVE}${libdir_nativesdk}/"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk gettext-native"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-initial-crosssdk"
