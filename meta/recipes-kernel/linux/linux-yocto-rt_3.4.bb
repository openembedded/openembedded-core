require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/preempt-rt/base"
KBRANCH_qemuppc = "standard/preempt-rt/qemuppc"

LINUX_VERSION ?= "3.4.28"
LINUX_KERNEL_TYPE = "preempt-rt"

KMETA = "meta"

SRCREV_machine ?= "3d56b103cd7072d520c395194e620aba2f6e52e3"
SRCREV_machine_qemuppc ?= "09178bc8825d6fb66ddcb1785f20a022c4ba2efd"
SRCREV_meta ?= "c0b1845314cebb21694dbb4459fcc6f80feb01d0"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " features/taskstats/taskstats.scc"
KERNEL_FEATURES_append_qemux86 = " cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
