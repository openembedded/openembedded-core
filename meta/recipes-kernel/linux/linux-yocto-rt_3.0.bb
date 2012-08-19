require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "yocto/standard/preempt-rt/base"
KBRANCH_qemuppc = "yocto/standard/preempt-rt/qemu-ppc32"

LINUX_VERSION ?= "3.0.32"
LINUX_KERNEL_TYPE = "preempt-rt"
KMETA = "meta"

SRCREV_machine ?= "bbd5bfe49403fdcca294ca4b163d5f7195ea3a8e"
SRCREV_machine_qemuppc ?= "c3575ad0016173b6e0b953404eb6a770e75a1f11"
SRCREV_meta ?= "bf5ee4945ee6d748e6abe16356f2357f76b5e2f0"

PR = "${INC_PR}.0"
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
