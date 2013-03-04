require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "0bed12bdbdd7a2aa39d8390b7a802dd72244b963"
SRCREV_machine_qemumips  ?= "4328dc8c1b73bdfe90656f217d99dc4ffa47da1f"
SRCREV_machine_qemuppc ?= "c12eb257c5fd9441b434c1216c3d987c89ecdd68"
SRCREV_machine_qemux86 ?= "3fa6521f19ce15c8e950042f843f33986ef2546d"
SRCREV_machine_qemux86-64 ?= "3fa6521f19ce15c8e950042f843f33986ef2546d"
SRCREV_machine ?= "3fa6521f19ce15c8e950042f843f33986ef2546d"
SRCREV_meta ?= "27988ba5eb2dd797a355e7d92fd78e78c7066757"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.1"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
