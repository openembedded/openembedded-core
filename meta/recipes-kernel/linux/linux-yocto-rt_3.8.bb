require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/preempt-rt/base"
KBRANCH_qemuppc = "standard/preempt-rt/qemuppc"

LINUX_VERSION ?= "3.8.4"
LINUX_KERNEL_TYPE = "preempt-rt"

KMETA = "meta"

SRCREV_machine ?= "d125901ac3e516e9b894df088f4282347651b690"
SRCREV_machine_qemuppc ?= "66630413d6df1665f2c5c94d25eb409271ffe20c"
SRCREV_meta ?= "2a6d36e75ca0a121570a389d7bab76ec240cbfda"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc features/taskstats/taskstats.scc"
KERNEL_FEATURES_append_qemux86=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
