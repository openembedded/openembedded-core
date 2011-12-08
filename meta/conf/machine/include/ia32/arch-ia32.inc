#
# IA32 Architecture definition
#

DEFAULTTUNE ?= "x86"
TARGET_FPU ?= ""
X86ARCH32 ?= "i586"
X86ARCH64 ?= "x86_64"

# ELF32 ABI
TUNEVALID[m32] = "IA32 ELF32 standard ABI"
TUNECONFLICTS[m32] = "m64 mx32"
TUNE_ARCH .= "${@bb.utils.contains("TUNE_FEATURES", "m32", "${X86ARCH32}", "" ,d)}"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "m32", "-m32", "", d)}"
MACHINEOVERRIDES .= "${@bb.utils.contains("TUNE_FEATURES", "m32", ":x86", "" ,d)}"

# x32 ABI
TUNEVALID[mx32] = "IA32e (x86_64) ELF32 standard ABI"
TUNECONFLICTS[mx32] = "m64 m32"
TUNE_ARCH .= "${@bb.utils.contains("TUNE_FEATURES", "mx32", "${X86ARCH64}", "" ,d)}"
ABIEXTENSION .= "${@bb.utils.contains("TUNE_FEATURES", "mx32", "x32", "" ,d)}"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "mx32", "-mx32", "", d)}"
TUNE_LDARGS += "${@bb.utils.contains("TUNE_FEATURES", "mx32", "-m elf32_x86_64", "", d)}"
TUNE_ASARGS += "${@bb.utils.contains("TUNE_FEATURES", "mx32", "-x32", "", d)}"

# ELF64 ABI
TUNEVALID[m64] = "IA32e (x86_64) ELF64 standard ABI"
TUNECONFLICT[m64] = "m32 mx32"
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

AVAILTUNES += "x86-64-x32"
TUNE_FEATURES_tune-x86-64-x32 ?= "mx32"
BASE_LIB_tune-x86-64-x32 ?= "libx32"
PACKAGE_EXTRA_ARCHS_tune-x86-64-x32 = "x86_64_x32"
TUNE_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "mx32", "_x32", "", d)}"
