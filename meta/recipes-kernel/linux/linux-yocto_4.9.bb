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

SRCREV_machine_qemuarm ?= "a4390ba8db101907133990f8e31ebcd56869ac3b"
SRCREV_machine_qemuarm64 ?= "24572abb2cad341dd79e6aafcb9cdd1a7f8a0c1e"
SRCREV_machine_qemumips ?= "580682c29df5023d2cecaf6c4074d401c4b57b94"
SRCREV_machine_qemuppc ?= "24572abb2cad341dd79e6aafcb9cdd1a7f8a0c1e"
SRCREV_machine_qemux86 ?= "24572abb2cad341dd79e6aafcb9cdd1a7f8a0c1e"
SRCREV_machine_qemux86-64 ?= "24572abb2cad341dd79e6aafcb9cdd1a7f8a0c1e"
SRCREV_machine_qemumips64 ?= "7c9ce18e05e1d84df17e677b26133671ec0430f5"
SRCREV_machine ?= "24572abb2cad341dd79e6aafcb9cdd1a7f8a0c1e"
SRCREV_meta ?= "e8095d45e15f02ffc953312bb41a57554b25a439"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.36"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
