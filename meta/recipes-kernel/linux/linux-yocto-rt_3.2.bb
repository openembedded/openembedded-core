require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/preempt-rt/base"
KBRANCH_qemuppc = "standard/preempt-rt/qemu-ppc32"

LINUX_VERSION ?= "3.2.32"
LINUX_KERNEL_TYPE = "preempt-rt"

KMETA = "meta"

SRCREV_machine ?= "d8f7347b67d909a999391c4b6f64447b0ccdc86a"
SRCREV_machine_qemuppc ?= "65c57e1a8656830260aa56a25bc8512dcb68258a"
SRCREV_meta ?= "e7f2fdc48f8808887175f0328274a2668084738c"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_FEATURES_append = "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " features/taskstats/taskstats.scc"
KERNEL_FEATURES_append_qemux86 = " cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound.scc"
