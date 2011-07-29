ARMPKGSFX_DSP = "${@bb.utils.contains("TUNE_FEATURES", [ "armv5", "dsp" ], "e", "", d)}"

require conf/machine/include/arm/arch-armv5.inc

# Little Endian
AVAILTUNES += "armv5e armv5te"
TUNE_FEATURES_tune-armv5e ?= "armv5 dsp"
TUNE_FEATURES_tune-armv5te ?= "armv5 dsp thumb"
PACKAGE_EXTRA_ARCHS_tune-armv5e = "${PACKAGE_EXTRA_ARCHS_tune-armv5} armv5e"
PACKAGE_EXTRA_ARCHS_tune-armv5te = "${PACKAGE_EXTRA_ARCHS_tune-armv5t} armv5e armv5te"

# Little Endian + VFP/DSP
AVAILTUNES += "armv5e-vfp armv5te-vfp armv5ehf-vfp armv5tehf-vfp"
TUNE_FEATURES_tune-armv5e-vfp ?= "armv5 vfp dsp"
TUNE_FEATURES_tune-armv5te-vfp ?= "armv5 vfp dsp thumb"
TUNE_FEATURES_tune-armv5ehf-vfp ?= "${TUNE_FEATURES_tune-armv5e-vfp} callconvention-hard"
TUNE_FEATURES_tune-armv5tehf-vfp ?= "${TUNE_FEATURES_tune-armv5te-vfp} callconvention-hard"
PACKAGE_EXTRA_ARCHS_tune-armv5e-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5-vfp} armv5e armv5e-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv5te-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5t-vfp} armv5e armv5te armv5e-vfp armv5te-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv5ehf-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5hf-vfp} armv5ehf-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv5tehf-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5thf-vfp} armv5ehf-vfp armv5tehf-vfp"

# Big Endian
AVAILTUNES += "armv5eb armv5teb"
TUNE_FEATURES_tune-armv5eb ?= "${TUNE_FEATURES_tune-armv5e} bigendian"
TUNE_FEATURES_tune-armv5teb ?= "${TUNE_FEATURES_tune-armv5te} bigendian"
PACKAGE_EXTRA_ARCHS_tune-armv5eb = "${PACKAGE_EXTRA_ARCHS_tune-armv5b} armv5eb"
PACKAGE_EXTRA_ARCHS_tune-armv5teb = "${PACKAGE_EXTRA_ARCHS_tune-armv5tb} armv5eb armv5teb"

# Big Endian + VFP/DSP
AVAILTUNES += "armv5eb-vfp armv5teb-vfp armv5ehfb-vfp armv5tehfb-vfp"
TUNE_FEATURES_tune-armv5eb-vfp ?= "${TUNE_FEATURES_tune-armv5e-vfp} bigendian"
TUNE_FEATURES_tune-armv5teb-vfp ?= "${TUNE_FEATURES_tune-armv5te-vfp} bigendian"
TUNE_FEATURES_tune-armv5ehfb-vfp ?= "${TUNE_FEATURES_tune-armv5eb-vfp} callconvention-hard"
TUNE_FEATURES_tune-armv5tehfb-vfp ?= "${TUNE_FEATURES_tune-armv5teb-vfp} callconvention-hard"
PACKAGE_EXTRA_ARCHS_tune-armv5eb-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5b-vfp} armv5eb armv5eb-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv5teb-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5tb-vfp} armv5eb armv5teb armv5eb-vfp armv5teb-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv5ehfb-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5hfb-vfp} armv5ehfb-vfp"
PACKAGE_EXTRA_ARCHS_tune-armv5tehfb-vfp = "${PACKAGE_EXTRA_ARCHS_tune-armv5thfb-vfp} armv5ehfb-vfp armv5tehfb-vfp"

