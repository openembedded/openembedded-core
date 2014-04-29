# The instruction set the compiler should use when generating application
# code. The kernel is always compiled with arm code at present. arm code
# is the original 32 bit ARM instruction set, thumb code is the 16 bit
# encoded RISC sub-set. Thumb code is smaller (maybe 70% of the ARM size)
# but requires more instructions (140% for 70% smaller code) so may be
# slower.
TUNEVALID[thumb] = "Use thumb instructions instead of ARM"
ARM_THUMB_OPT = "${@['arm', 'thumb'][d.getVar('ARM_INSTRUCTION_SET', True) == 'thumb']}"
ARM_THUMB_SUFFIX .= "${@bb.utils.contains('TUNE_FEATURES', 'armv4',  't',  '', d)}"
ARM_THUMB_SUFFIX .= "${@bb.utils.contains('TUNE_FEATURES', 'armv5',  't',  '', d)}"
ARM_THUMB_SUFFIX .= "${@bb.utils.contains('TUNE_FEATURES', 'armv6',  't',  '', d)}"
ARM_THUMB_SUFFIX .= "${@bb.utils.contains('TUNE_FEATURES', 'armv7a', 't2', '', d)}"
ARM_THUMB_SUFFIX .= "${@bb.utils.contains('TUNE_FEATURES', 'armv7r', 't2', '', d)}"
ARM_THUMB_SUFFIX .= "${@bb.utils.contains('TUNE_FEATURES', 'armv7m', 't2', '', d)}"

# If the device supports ARM, then respect ARM_THUMB_OPT (which can be "arm" or "thumb")
# If the defice doesn't support ARM, then always set "thumb" even when
# some recipe explicitly sets ARM_INSTRUCTION_SET to "arm"
ARM_M_OPT = "${@bb.utils.contains('TUNE_FEATURES', 'arm', '${ARM_THUMB_OPT}', 'thumb', d)}"
python () {
    selected = d.getVar('ARM_INSTRUCTION_SET', True)
    if selected == None:
        return
    used = d.getVar('ARM_M_OPT', True)
    if selected != used:
        pn = d.getVar('PN', True)
        bb.warn("Recipe '%s' selects ARM_INSTRUCTION_SET to be '%s', but tune configuration overrides it to '%s'" % (pn, selected, used))
}

TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'thumb', ' -m${ARM_M_OPT}', '', d)}"
OVERRIDES .= "${@bb.utils.contains('TUNE_FEATURES', 'thumb', ':thumb', '', d)}"

ARMPKGSFX_THUMB .= "${@bb.utils.contains('TUNE_FEATURES', 'thumb', '${ARM_THUMB_SUFFIX}', '', d)}"

# Whether to compile with code to allow interworking between the two
# instruction sets. This allows thumb code to be executed on a primarily
# arm system and vice versa. It is strongly recommended that DISTROs not
# turn this off - the actual cost is very small.
TUNEVALID[no-thumb-interwork] = "Disable mixing of thumb and ARM functions"
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'no-thumb-interwork', ' -mno-thumb-interwork', ' -mthumb-interwork', d)}"
OVERRIDES .= "${@bb.utils.contains('TUNE_FEATURES', 'no-thumb-interwork', ':thumb-interwork', '', d)}"

# what about armv7m devices which don't support -marm (e.g. Cortex-M3)?
TARGET_CC_KERNEL_ARCH += "-mno-thumb-interwork -marm"
