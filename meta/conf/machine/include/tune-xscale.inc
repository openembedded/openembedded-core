TUNE_ARCH = "arm"

INHERIT += "siteinfo"

TUNE_CCARGS = "-march=armv5te -mtune=xscale"
TARGET_CC_KERNEL_ARCH = "-march=armv5te -mtune=xscale"
TUNE_PKGARCH = "${@['armv5teb', 'armv5te'][bb.data.getVar('SITEINFO_ENDIANESS', d, 1) == 'le']}"
PACKAGE_EXTRA_ARCHS = "${@['armeb armv4b armv4tb armv5teb', 'arm armv4 armv4t armv5te'][bb.data.getVar('SITEINFO_ENDIANESS', d, 1) == 'le']}"

# webkit-gtk has alignment issues with double instructions on armv5 so
# disable them here
TUNE_CCARGS_pn-webkit-gtk = "-march=armv4t"
TUNE_CCARGS_pn-cairo = "-march=armv4t"
