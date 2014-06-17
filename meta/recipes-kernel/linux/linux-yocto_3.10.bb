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

SRCREV_machine_qemuarm ?= "cb8bf1fb82bb1d00fadc92608f5fd018b2dbf556"
SRCREV_machine_qemumips ?= "7cbc39735f2cc708858d8319b96338b750ec76cb"
SRCREV_machine_qemuppc ?= "15e1d5d7380a13907016e1f4c941026580e810a9"
SRCREV_machine_qemux86 ?= "ca510b5192c3b3814f1d1a19403d8847ba5db12b"
SRCREV_machine_qemux86-64 ?= "ca510b5192c3b3814f1d1a19403d8847ba5db12b"
SRCREV_machine_qemumips64 ?= "34bd311a9ebb5b1f44a156f5e9d61646a6077c5f"
SRCREV_machine ?= "ca510b5192c3b3814f1d1a19403d8847ba5db12b"
SRCREV_meta ?= "b6d95bb5bf6b9e9b5c149e68ffed6db7a58b4187"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.41"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
