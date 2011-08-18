DEFAULTTUNE ?= "armv7a"

ARMPKGARCH ?= "armv7a"

TUNEVALID[armv7-a] = "Enable instructions for ARMv7-a"
TUNE_CONFLICTS[armv7a] = "armv4 armv5 armv6 armv7"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "armv7a", "-march=armv7-a -fno-tree-vectorize", "", d)}"

require conf/machine/include/arm/arch-armv7.inc

# Little Endian base configs
AVAILTUNES += "armv7a armv7at armv7a-neon armv7at-neon"
TUNE_FEATURES_tune-armv7a ?= "armv7a vfp"
TUNE_FEATURES_tune-armv7at ?= "armv7a vfp thumb"
TUNE_FEATURES_tune-armv7a-neon ?= "armv7a vfp neon"
TUNE_FEATURES_tune-armv7at-neon ?= "armv7a vfp neon thumb"
PACKAGE_EXTRA_ARCHS_tune-armv7a = "${PACKAGE_EXTRA_ARCHS_tune-armv7} armv7a armv7a-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7a-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7a} armv7a-vfp-neon"
PACKAGE_EXTRA_ARCHS_tune-armv7at = "${PACKAGE_EXTRA_ARCHS_tune-armv7t} armv7a armv7a-vfp armv7at2-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7at-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7at} armv7a-vfp-neon armv7at2-vfp-neon"

# VFP Tunes
AVAILTUNES += "armv7hf armv7thf armv7hf-neon armv7thf-neon"
TUNE_FEATURES_tune-armv7ahf ?= "${TUNE_FEATURES_tune-armv7a} callconvention-hard"
TUNE_FEATURES_tune-armv7athf ?= "${TUNE_FEATURES_tune-armv7at} callconvention-hard"
TUNE_FEATURES_tune-armv7ahf-neon ?= "${TUNE_FEATURES_tune-armv7a-neon} callconvention-hard"
TUNE_FEATURES_tune-armv7athf-neon ?= "${TUNE_FEATURES_tune_armv7at-neon} callconvention-hard"
PACKAGE_EXTRA_ARCHS_tune-armv7ahf = "${PACKAGE_EXTRA_ARCHS_tune-armv7hf} armv7ahf-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7athf = "${PACKAGE_EXTRA_ARCHS_tune-armv7thf} armv7ahf-vfp armv7at2hf-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7ahf-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7ahf} armv7ahf-vfp-neon"
PACKAGE_EXTRA_ARCHS_tune-armv7athf-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7athf} armv7ahf-vfp-neon armv7at2hf-vfp-neon"

# Big Endian
AVAILTUNES += "armv7ab armv7atb armv7ab-neon armv7atb-neon"
TUNE_FEATURES_tune-armv7ab ?= "${TUNE_FEATURES_tune-armv7a} bigendian"
TUNE_FEATURES_tune-armv7atb ?= "${TUNE_FEATURES_tune-armv7at} bigendian"
TUNE_FEATURES_tune-armv7ab-neon ?= "${TUNE_FEATURES_tune-armv7a-neon} bigendian"
TUNE_FEATURES_tune-armv7atb-neon ?= "${TUNE_FEATURES_tune-armv7at-neon} bigendian"
PACKAGE_EXTRA_ARCHS_tune-armv7ab = "${PACKAGE_EXTRA_ARCHS_tune-armv7} armv7ab-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7atb = "${PACKAGE_EXTRA_ARCHS_tune-armv7t} armv7ab-vfp armv7at2b-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7ab-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7a} armv7ab-vfp-neon"
PACKAGE_EXTRA_ARCHS_tune-armv7atb-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7at} armv7ab-vfp-neon armv7at2b-vfp-neon"

# Big Endian + VFP
AVAILTUNES += "armv7ahfb armv7athfb armv7ahfb-neon armv7athfb-neon"
TUNE_FEATURES_tune-armv7ahfb ?= "${TUNE_FEATURES_tune-armv7ab} callconvention-hard"
TUNE_FEATURES_tune-armv7athfb ?= "${TUNE_FEATURES_tune-armv7atb} callconvention-hard"
TUNE_FEATURES_tune-armv7ahfb-neon ?= "${TUNE_FEATURES_tune-armv7ab-neon} callconvention-hard"
TUNE_FEATURES_tune-armv7athfb-neon ?= "${TUNE_FEATURES_tune_armv7atb-neon} callconvention-hard"
PACKAGE_EXTRA_ARCHS_tune-armv7ahfb = "${PACKAGE_EXTRA_ARCHS_tune-armv7hfb} armv7ahfb-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7athfb = "${PACKAGE_EXTRA_ARCHS_tune-armv7thfb} armv7ahfb-vfp armv7at2hfb-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv7ahfb-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7ahfb} armv7ahfb-vfp-neon"
PACKAGE_EXTRA_ARCHS_tune-armv7athfb-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7athfb} armv7ahfb-vfp-neon armv7at2hfb-vfp-neon"

