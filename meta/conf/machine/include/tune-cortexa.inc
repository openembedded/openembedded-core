DEFAULTTUNE ?= "armv7a-neon"

require conf/machine/include/arm/arch-armv7a.inc

TUNE_CCARGS .= "${@bb.utils.contains("TUNE_FEATURES", "cortex${CORTEX_ID}", " -mcpu=cortex-${CORTEX_ID}", "", d)}"

# Little Endian base configs
AVAILTUNES += "cortex${CORTEX_ID} cortex${CORTEX_ID}t cortex${CORTEX_ID}-neon cortex${CORTEX_ID}t-neon"
ARMPKGARCH_tune-cortex${CORTEX_ID} = "cortex${CORTEX_ID}"
ARMPKGARCH_tune-cortex${CORTEX_ID}t = "cortex${CORTEX_ID}"
ARMPKGARCH_tune-cortex${CORTEX_ID}-neon = "cortex${CORTEX_ID}"
ARMPKGARCH_tune-cortex${CORTEX_ID}t-neon = "cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID} = "${TUNE_FEATURES_tune-armv7a} cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}t = "${TUNE_FEATURES_tune-armv7at} cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}-neon = "${TUNE_FEATURES_tune-armv7a-neon} cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}t-neon = "${TUNE_FEATURES_tune-armv7at-neon} cortex${CORTEX_ID}"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID} = "${PACKAGE_EXTRA_ARCHS_tune-armv7a} cortex${CORTEX_ID}-vfp"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}t = "${PACKAGE_EXTRA_ARCHS_tune-armv7at} cortex${CORTEX_ID}-vfp cortex${CORTEX_ID}t2-vfp"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7a-neon} cortex${CORTEX_ID}-vfp cortex${CORTEX_ID}-vfp-neon"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}t-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7at-neon} cortex${CORTEX_ID}-vfp cortex${CORTEX_ID}-vfp-neon cortex${CORTEX_ID}t2-vfp cortex${CORTEX_ID}t2-vfp-neon"

# VFP Tunes
AVAILTUNES += "cortex${CORTEX_ID}hf cortex${CORTEX_ID}thf cortex${CORTEX_ID}hf-neon cortex${CORTEX_ID}thf-neon"
ARMPKGARCH_tune-cortex${CORTEX_ID}hf = "cortex${CORTEX_ID}"
ARMPKGARCH_tune-cortex${CORTEX_ID}thf = "cortex${CORTEX_ID}"
ARMPKGARCH_tune-cortex${CORTEX_ID}hf-neon = "cortex${CORTEX_ID}"
ARMPKGARCH_tune-cortex${CORTEX_ID}thf-neon = "cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}hf ?= "${TUNE_FEATURES_tune-armv7ahf} cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}thf ?= "${TUNE_FEATURES_tune-armv7athf} cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}hf-neon ?= "${TUNE_FEATURES_tune-armv7ahf-neon} cortex${CORTEX_ID}"
TUNE_FEATURES_tune-cortex${CORTEX_ID}thf-neon ?= "${TUNE_FEATURES_tune-armv7athf-neon} cortex${CORTEX_ID}"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}hf = "${PACKAGE_EXTRA_ARCHS_tune-armv7ahf} cortex${CORTEX_ID}hf-vfp"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}thf = "${PACKAGE_EXTRA_ARCHS_tune-armv7athf} cortex${CORTEX_ID}hf-vfp cortex${CORTEX_ID}t2hf-vfp"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}hf-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7ahf-neon} cortex${CORTEX_ID}hf-vfp cortex${CORTEX_ID}hf-vfp-neon"
PACKAGE_EXTRA_ARCHS_tune-cortex${CORTEX_ID}thf-neon = "${PACKAGE_EXTRA_ARCHS_tune-armv7athf-neon} cortex${CORTEX_ID}hf-vfp cortex${CORTEX_ID}hf-vfp-neon cortex${CORTEX_ID}t2hf-vfp cortex${CORTEX_ID}t2hf-vfp-neon"
