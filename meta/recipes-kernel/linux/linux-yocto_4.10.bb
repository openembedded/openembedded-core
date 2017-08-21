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

SRCREV_machine_qemuarm ?= "3926e386306fe0ae92feae45bc47e4c013daa1fc"
SRCREV_machine_qemuarm64 ?= "6648a34e00c55a0634b39e661dd6ba14dd106473"
SRCREV_machine_qemumips ?= "a038a5932f3be820f42d78f4e47850987ad72ce3"
SRCREV_machine_qemuppc ?= "6648a34e00c55a0634b39e661dd6ba14dd106473"
SRCREV_machine_qemux86 ?= "6648a34e00c55a0634b39e661dd6ba14dd106473"
SRCREV_machine_qemux86-64 ?= "6648a34e00c55a0634b39e661dd6ba14dd106473"
SRCREV_machine_qemumips64 ?= "b442c964432938cbe10c4cc578260353045af980"
SRCREV_machine ?= "6648a34e00c55a0634b39e661dd6ba14dd106473"
SRCREV_meta ?= "ba079c7ed914d6502351da16c6b4ccf1e285ebdc"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10.17"

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
