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

SRCREV_machine_qemuarm ?= "5c60622f4af68de9d6d9320f7d785156d01c305c"
SRCREV_machine_qemuarm64 ?= "cf9a7dd9f41a32c10d19a51497463d6ec2991107"
SRCREV_machine_qemumips ?= "a5673234ab47a8653b7c3629811cc9ca634fcadb"
SRCREV_machine_qemuppc ?= "cf9a7dd9f41a32c10d19a51497463d6ec2991107"
SRCREV_machine_qemux86 ?= "cf9a7dd9f41a32c10d19a51497463d6ec2991107"
SRCREV_machine_qemux86-64 ?= "cf9a7dd9f41a32c10d19a51497463d6ec2991107"
SRCREV_machine_qemumips64 ?= "7b4d796b512ed9bb1e13af089fc4d20a35187eeb"
SRCREV_machine ?= "cf9a7dd9f41a32c10d19a51497463d6ec2991107"
SRCREV_meta ?= "f16cac53436be696fa055bc4a139f3f1dc39a9ce"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.46"

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
