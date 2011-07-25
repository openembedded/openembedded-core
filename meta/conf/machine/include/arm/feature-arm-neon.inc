TUNEVALID[neon] = "Enable Neon SIMD accelerator unit."
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "neon", "-mfpu=neon", "" ,d)}"
ARMPKGSFX_FPU .= "${@bb.utils.contains("TUNE_FEATURES", "neon", "-neon", "" ,d)}"
