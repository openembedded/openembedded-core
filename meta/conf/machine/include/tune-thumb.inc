#tune file for thumb instructions

ARM_INSTRUCTION_SET ?= "arm"
# "arm" "thumb"
#    The instruction set the compiler should use when generating application
#    code.  The kernel is always compiled with arm code at present.  arm code
#    is the original 32 bit ARM instruction set, thumb code is the 16 bit
#    encoded RISC sub-set.  Thumb code is smaller (maybe 70% of the ARM size)
#    but requires more instructions (140% for 70% smaller code) so may be
#    slower.

THUMB_INTERWORK ?= "yes"
# "yes" "no"
#    Whether to compile with code to allow interworking between the two
#    instruction sets.  This allows thumb code to be executed on a primarily
#    arm system and vice versa.  It is strongly recommended that DISTROs not
#    turn this off - the actual cost is very small.

OVERRIDE_THUMB = "${@['', ':thumb'][bb.data.getVar('ARM_INSTRUCTION_SET', d, 1) == 'thumb']}"
OVERRIDE_INTERWORK = "${@['', ':thumb-interwork'][bb.data.getVar('THUMB_INTERWORK', d, 1) == 'yes']}"
OVERRIDES .= "${OVERRIDE_THUMB}${OVERRIDE_INTERWORK}"

#    Compiler and linker options for application code and kernel code.  These
#    options ensure that the compiler has the correct settings for the selected
#    instruction set and interworking.
ARM_INTERWORK_M_OPT = "${@['-mno-thumb-interwork', '-mthumb-interwork'][bb.data.getVar('THUMB_INTERWORK', d, 1) == 'yes']}"
ARM_THUMB_M_OPT = "${@['-mno-thumb', '-mthumb'][bb.data.getVar('ARM_INSTRUCTION_SET', d, 1) == 'thumb']}"

#
TUNE_CCARGS +=  "${ARM_INTERWORK_M_OPT} ${ARM_THUMB_M_OPT}"
TARGET_CC_KERNEL_ARCH  += "-mno-thumb-interwork -mno-thumb"

