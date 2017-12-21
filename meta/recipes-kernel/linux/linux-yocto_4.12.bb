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

SRCREV_machine_qemuarm ?= "01295fb92f5b47c5ebdadd63497cc1f964a873ff"
SRCREV_machine_qemuarm64 ?= "558fe84d691abbb8c8f5e149aa29ef4a478d0128"
SRCREV_machine_qemumips ?= "674e17014797e9b085474eb6fef5bd6193a43218"
SRCREV_machine_qemuppc ?= "558fe84d691abbb8c8f5e149aa29ef4a478d0128"
SRCREV_machine_qemux86 ?= "558fe84d691abbb8c8f5e149aa29ef4a478d0128"
SRCREV_machine_qemux86-64 ?= "558fe84d691abbb8c8f5e149aa29ef4a478d0128"
SRCREV_machine_qemumips64 ?= "aea1b0ab4353dfdb326f40a913006261e37d2834"
SRCREV_machine ?= "558fe84d691abbb8c8f5e149aa29ef4a478d0128"
SRCREV_meta ?= "b66a4f9730339b3c0c4af1db03dd26da71e419d5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.18"

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
