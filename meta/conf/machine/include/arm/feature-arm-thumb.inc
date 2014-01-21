# The instruction set the compiler should use when generating application
# code. The kernel is always compiled with arm code at present. arm code
# is the original 32 bit ARM instruction set, thumb code is the 16 bit
# encoded RISC sub-set. Thumb code is smaller (maybe 70% of the ARM size)
# but requires more instructions (140% for 70% smaller code) so may be
# slower.
TUNEVALID[thumb] = "Use thumb instructions instead of ARM"
ARM_THUMB_M_OPT = "${@['-marm', '-mthumb'][d.getVar('ARM_INSTRUCTION_SET', True) == 'thumb']}"
TUNE_CCARGS .= "${@bb.utils.contains("TUNE_FEATURES", "thumb", " ${ARM_THUMB_M_OPT}", "", d)}"
OVERRIDES .= "${@bb.utils.contains("TUNE_FEATURES", "thumb", ":thumb", "", d)}"

ARMPKGSFX_THUMB .= "${@bb.utils.contains("TUNE_FEATURES", [ "armv4", "thumb" ], "t", "", d)}"
ARMPKGSFX_THUMB .= "${@bb.utils.contains("TUNE_FEATURES", [ "armv5", "thumb" ], "t", "", d)}"
ARMPKGSFX_THUMB .= "${@bb.utils.contains("TUNE_FEATURES", [ "armv6", "thumb" ], "t", "", d)}"
ARMPKGSFX_THUMB .= "${@bb.utils.contains("TUNE_FEATURES", [ "armv7a", "thumb" ], "t2", "", d)}"

# Whether to compile with code to allow interworking between the two
# instruction sets. This allows thumb code to be executed on a primarily
# arm system and vice versa. It is strongly recommended that DISTROs not
# turn this off - the actual cost is very small.
TUNEVALID[no-thumb-interwork] = "Disable mixing of thumb and ARM functions"
TUNE_CCARGS .= "${@bb.utils.contains("TUNE_FEATURES", "no-thumb-interwork", " -mno-thumb-interwork", " -mthumb-interwork", d)}"
OVERRIDES .= "${@bb.utils.contains("TUNE_FEATURES", "no-thumb-interwork", ":thumb-interwork", "", d)}"

TARGET_CC_KERNEL_ARCH += "-mno-thumb-interwork -marm"
