DEFAULTTUNE ?= "xscale"

require conf/machine/include/arm/arch-armv5-dsp.inc

TUNEVALID[xscale] = "Enable PXA255/PXA26x Xscale specific processor optimizations"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "xscale", "-mtune=xscale", "", d)}"

AVAILTUNES += "xscale"
TUNE_FEATURES_tune-xscale = "${TUNE_FEATURES_tune-armv5te} xscale"
PACKAGE_EXTRA_ARCHS_tune-xscale = "${PACKAGE_EXTRA_ARCHS_tune-armv5te}"

AVAILTUNES += "xscale-be"
TUNE_FEATURES_tune-xscale-be = "${TUNE_FEATURES_tune-armv5teb} xscale-be"
PACKAGE_EXTRA_ARCHS_tune-xscale-be = "${PACKAGE_EXTRA_ARCHS_tune-armv5teb}"

# webkit-gtk has alignment issues with double instructions on armv5 so
# disable them here
TUNE_CCARGS_pn-webkit-gtk = "-march=armv4t"
TUNE_CCARGS_pn-cairo = "-march=armv4t"
