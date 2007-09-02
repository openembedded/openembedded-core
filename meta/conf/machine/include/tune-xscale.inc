FEED_ARCH = "armv5te"

TARGET_CC_ARCH = "-march=armv5te -mtune=xscale"
TARGET_CC_KERNEL_ARCH = "-march=armv5te -mtune=xscale"
PACKAGE_ARCH = "${@['armv5teb', 'armv5te'][bb.data.getVar('SITEINFO_ENDIANESS', d, 1) == 'le']}"
