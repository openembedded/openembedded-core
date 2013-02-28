require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "c7cc288f0113433bd51d4958d36d4ef4bf13c214"
SRCREV_machine_qemumips  ?= "d4f5af54aa6a3592c6b2656ef20ea98bee14fde9"
SRCREV_machine_qemuppc ?= "8afd98bd2aae1ad86b97d4333107408745518f8d"
SRCREV_machine_qemux86 ?= "840bb8c059418c4753415df56c9aff1c0d5354c8"
SRCREV_machine_qemux86-64 ?= "840bb8c059418c4753415df56c9aff1c0d5354c8"
SRCREV_machine ?= "840bb8c059418c4753415df56c9aff1c0d5354c8"
SRCREV_meta ?= "4fd76cc4f33e0afd8f906b1e8f231b6d13b6c993"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.34"

PR = "${INC_PR}.3"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
