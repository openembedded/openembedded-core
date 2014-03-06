require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "c295c53987007a5d0c87cc9b23125e9ff85900bd"
SRCREV_machine_qemumips ?= "2833be8c96671b24a203a5dc45a21c38d5d841c3"
SRCREV_machine_qemuppc ?= "cf413ddca8b974a473b72635e1499223a02d7949"
SRCREV_machine_qemux86 ?= "78afd3095c9b37efbbfbfdc25eb3833ef3c6a718"
SRCREV_machine_qemux86-64 ?= "78afd3095c9b37efbbfbfdc25eb3833ef3c6a718"
SRCREV_machine_qemumips64 ?= "3e1899546c014808fed8d336b777bf8d81f50d7b"
SRCREV_machine ?= "78afd3095c9b37efbbfbfdc25eb3833ef3c6a718"
SRCREV_meta ?= "6e0e756d51372c8b176c5d1e6f786545bceed351"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.32"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
