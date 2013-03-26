require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "358940bfd596167b6ed03caa4113944c7845a855"
SRCREV_machine_qemumips  ?= "7d995e09dfb21185442d37f62db0a5655b1adba3"
SRCREV_machine_qemuppc ?= "1c65b040e7952d177ac4c83df5551efd90fc29f2"
SRCREV_machine_qemux86 ?= "c753c612377f08f5172b528b9b07715f2b3b5735"
SRCREV_machine_qemux86-64 ?= "c753c612377f08f5172b528b9b07715f2b3b5735"
SRCREV_machine ?= "c753c612377f08f5172b528b9b07715f2b3b5735"
SRCREV_meta ?= "2a6d36e75ca0a121570a389d7bab76ec240cbfda"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.4"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
