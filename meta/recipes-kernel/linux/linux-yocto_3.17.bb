KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "a1183153c56ace9f9140c73ce730ec181f78dc0f"
SRCREV_machine_qemumips ?= "18c8db2009f25524a29a5258d041e0442c10817d"
SRCREV_machine_qemuppc ?= "6d2bbdac0e6560542fa2252d91eee9ae0f17f9c7"
SRCREV_machine_qemux86 ?= "5ff54d8fbf74278e9e5074cbba516a14f0915ff7"
SRCREV_machine_qemux86-64 ?= "5ff54d8fbf74278e9e5074cbba516a14f0915ff7"
SRCREV_machine_qemumips64 ?= "2306cb2dd019c8b731879331ba314035a7271e7b"
SRCREV_machine ?= "5ff54d8fbf74278e9e5074cbba516a14f0915ff7"
SRCREV_meta ?= "b81030f9ec2de3dc6c048e142dcbff62e305cc40"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17.6"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
