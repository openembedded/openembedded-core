# MIPS Architecture definition
# 12 defined ABIs, all combinations of:
# *) Big/Little Endian
# *) Hardware/Software Floating Point
# *) o32, n32, n64 ABI

DEFAULTTUNE ?= "mips"

# Endianess
TUNEVALID[bigendian] = "Enable big-endian mode"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "bigendian", "-meb", "-mel", d)}"

# ABI flags
TUNEVALID[o32] = "MIPS o32 ABI"
TUNECONFLICT[o32] = "n32 n64"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "o32", "-mabi=32", "", d)}"

TUNEVALID[n32] = "MIPS64 n32 ABI"
TUNECONFLICT[n32] = "o32 n64"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "n32", "-mabi=n32", "", d)}"

TUNEVALID[n64] = "MIPS64 n64 ABI"
TUNECONFLICT[n64] = "o32 n32"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "n64", "-mabi=64", "", d)}"

# Floating point
TUNEVALID[fpu-hard] = "Use hardware FPU"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard", "-mhard-float", "-msoft-float", d)}"
TARGET_FPU = "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard", "", "soft", d)}"

# Package naming
MIPSPKGSFX_ENDIAN = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", "", "el", d)}"
MIPSPKGSFX_BYTE = "${@bb.utils.contains("TUNE_FEATURES", "n64" , "64", "", d)}"
MIPSPKGSFX_BYTE .= "${@bb.utils.contains("TUNE_FEATURES", "n32" , "64", "", d)}"
MIPSPKGSFX_FPU = "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard" , "", "-nf", d)}"
MIPSPKGSFX_ABI = "${@bb.utils.contains("TUNE_FEATURES", "n32", "-n32", "", d)}"

TUNE_ARCH = "mips${MIPSPKGSFX_BYTE}${MIPSPKGSFX_ENDIAN}"
TUNE_PKGARCH ?= "${TUNE_ARCH}${MIPSPKGSFX_FPU}${MIPSPKGSFX_ABI}"

# Base tunes
AVAILTUNES += "mips mips64-n32 mips64 mipsel mips64el-n32 mips64el mips-nf mips64-nf-n32 mips64-nf mipsel-nf mips64el-nf-n32 mips64el-nf"
TUNE_FEATURES_tune-mips = "o32 bigendian fpu-hard"
BASE_LIB_tune-mips = "lib"
TUNE_FEATURES_tune-mips64-n32 = "n32 bigendian fpu-hard"
BASE_LIB_tune-mips64-n32 = "lib32"
TUNE_FEATURES_tune-mips64 = "n64 bigendian fpu-hard"
BASE_LIB_tune-mips64 = "lib64"
TUNE_FEATURES_tune-mipsel = "o32 fpu-hard"
BASE_LIB_tune-mipsel = "lib"
TUNE_FEATURES_tune-mips64el-n32 = "n32 fpu-hard"
BASE_LIB_tune-mips64el-n32 = "lib32"
TUNE_FEATURES_tune-mips64el = "n64 fpu-hard"
BASE_LIB_tune-mips64el = "lib64"
TUNE_FEATURES_tune-mips-nf = "o32 bigendian"
BASE_LIB_tune-mips-nf = "lib"
TUNE_FEATURES_tune-mips64-nf-n32 = "n32 bigendian"
BASE_LIB_tune-mips64-nf-n32 = "lib32"
TUNE_FEATURES_tune-mips64-nf = "n64 bigendian"
BASE_LIB_tune-mips64-nf = "lib64"
TUNE_FEATURES_tune-mipsel-nf = "o32"
BASE_LIB_tune-mipsel-nf = "lib"
TUNE_FEATURES_tune-mips64el-nf-n32 = "n32"
BASE_LIB_tune-mips64el-nf-n32 = "lib32"
TUNE_FEATURES_tune-mips64el-nf = "n64"
BASE_LIB_tune-mips64el-nf = "lib64"

