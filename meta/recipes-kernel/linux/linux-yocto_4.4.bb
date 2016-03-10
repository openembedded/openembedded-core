KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "861adc8d32fbb0c1f14fb9b7b67222ab711473a1"
SRCREV_machine_qemuarm64 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"
SRCREV_machine_qemumips ?= "0b21ee5897cc49c94f42fca9cea4665f76ae7e0e"
SRCREV_machine_qemuppc ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"
SRCREV_machine_qemux86 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"
SRCREV_machine_qemux86-64 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"
SRCREV_machine_qemumips64 ?= "8b25338045fdb88a9cdff808003102643f03083e"
SRCREV_machine ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"
SRCREV_meta ?= "ce5b35bc76bb65d93d0897535c088f51dae4048b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.3"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
