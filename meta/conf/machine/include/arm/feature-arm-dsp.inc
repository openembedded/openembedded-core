# Note: TUNEVALID set in conf/machine/include/arm/arch-armv5-dsp.inc
TUNE_CCARGS_MARCH_OPTS .= "${@bb.utils.contains('TUNE_FEATURES', [ 'dsp' ], '+dsp', '', d)}"
