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

SRCREV_machine_qemuarm ?= "20544507201f870a365c43759e5dea1ab49a2d68"
SRCREV_machine_qemuarm64 ?= "3eab887a55424fc2c27553b7bfe32330df83f7b8"
SRCREV_machine_qemumips ?= "03d4caf37d133a923e49b8ad6d814ee299cf92c7"
SRCREV_machine_qemuppc ?= "3eab887a55424fc2c27553b7bfe32330df83f7b8"
SRCREV_machine_qemux86 ?= "a7d71794e4e38d2f861c1b1dbff761ae0b0836b3"
SRCREV_machine_qemux86-64 ?= "a7d71794e4e38d2f861c1b1dbff761ae0b0836b3"
SRCREV_machine_qemumips64 ?= "a4793b209b32964533e37ebd28a72b757c0f651a"
SRCREV_machine ?= "a7d71794e4e38d2f861c1b1dbff761ae0b0836b3"
SRCREV_meta ?= "dd820fa5f7079ac75338f0484151e6454db06951"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8-rc4"

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
