#
# IA32 Architecture definition
#

DEFAULTTUNE ?= "x86"
TARGET_FPU ?= ""
X86ARCH32 ?= "i586"
X86ARCH64 ?= "x86_64"

# ELF32 ABI
TUNEVALID[m32] = "IA32 ELF32 standard ABI"
TUNECONFLICTS[m32] = "m64"
TUNE_ARCH .= "${@bb.utils.contains("TUNE_FEATURES", "m32", "${X86ARCH32}", "" ,d)}"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "m32", "-m32", "", d)}"

# ELF64 ABI
TUNEVALID[m64] = "IA32e (x86_64) ELF64 standard ABI"
TUNECONFLICT[m64] = "m32"
TUNE_ARCH .= "${@bb.utils.contains("TUNE_FEATURES", "m64", "${X86ARCH64}", "" ,d)}"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "m64", "-m64", "", d)}"

TUNE_PKGARCH ?= "${@bb.utils.contains("TUNE_FEATURES", "m32", "x86", "x86_64", d)}"

# Default Tune configurations
AVAILTUNES += "x86"
TUNE_FEATURES_tune-x86 ?= "m32"
BASE_LIB_tune-x86 ?= "lib"
PACKAGE_EXTRA_ARCHS_tune-x86 = "x86"

AVAILTUNES += "x86-64"
TUNE_FEATURES_tune-x86-64 ?= "m64"
BASE_LIB_tune-x86-64 ?= "lib64"
PACKAGE_EXTRA_ARCHS_tune-x86-64 = "x86_64"
