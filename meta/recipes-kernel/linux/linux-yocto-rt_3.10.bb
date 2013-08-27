require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/preempt-rt/base"
KBRANCH_qemuppc = "standard/preempt-rt/qemuppc"

SRCREV_machine ?= "77c5842818867366c3d08ca1dec19dc43aee0e91"
SRCREV_machine_qemuppc ?= "7be83bc1124d2a2c75f99df94baaf718659722ea"
SRCREV_meta ?= "1b9b113837f353fd94148beb6bd00a3b7b35d01d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.10.9"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

LINUX_KERNEL_TYPE = "preempt-rt"

COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm|qemuppc|qemumips)"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc features/taskstats/taskstats.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
