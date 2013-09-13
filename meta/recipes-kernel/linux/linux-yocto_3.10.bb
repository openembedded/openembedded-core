require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "4901b46258e7195bd4d4ff14a618a2cb71395bf3"
SRCREV_machine_qemumips  ?= "bc6be45f32ef091ff13b16ba0f74f611232e5d21"
SRCREV_machine_qemuppc ?= "789d821269f0496e95ed953f2d1126c4eb39c591"
SRCREV_machine_qemux86 ?= "702040ac7c7ec66a29b4d147665ccdd0ff015577"
SRCREV_machine_qemux86-64 ?= "702040ac7c7ec66a29b4d147665ccdd0ff015577"
SRCREV_machine_qemumips64 ?= "6b3a777e3724d30ba03d88d75698fa0734c5a73f"
SRCREV_machine ?= "702040ac7c7ec66a29b4d147665ccdd0ff015577"
SRCREV_meta ?= "285f93bf942e8f6fa678ffc6cc53696ed5400718"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.11"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
