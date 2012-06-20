inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "yocto/standard/preempt-rt/base"
KBRANCH_qemuppc = "yocto/standard/preempt-rt/qemu-ppc32"

LINUX_VERSION ?= "3.0.32"
LINUX_KERNEL_TYPE = "preempt-rt"
KMETA = "meta"

SRCREV_machine ?= "e67428d9966eecec4c081993dc64ceb5c0e64643"
SRCREV_machine_qemuppc ?= "dcca458cb92cc287f70e4062f02460f36a881b16"
SRCREV_meta ?= "46e8fc2bbbe73514e8d99101adaaa373f760ffa7"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_FEATURES = "features/netfilter"
KERNEL_FEATURES_append = " features/taskstats"
KERNEL_FEATURES_append_qemux86 = " cfg/sound"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound"
