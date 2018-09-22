KBRANCH ?= "v4.18/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.18/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.18/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.18/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.18/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.18/standard/base"
KBRANCH_qemux86-64 ?= "v4.18/standard/base"
KBRANCH_qemumips64 ?= "v4.18/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "ef0043f38afb743bffdc39aacc54ea2419da0be3"
SRCREV_machine_qemuarm64 ?= "0cdc8564c61958a39704d97e008120bd7c762f60"
SRCREV_machine_qemumips ?= "a6d4477ea91aec9d3d6af538f8f9d27a687540aa"
SRCREV_machine_qemuppc ?= "0cdc8564c61958a39704d97e008120bd7c762f60"
SRCREV_machine_qemux86 ?= "0cdc8564c61958a39704d97e008120bd7c762f60"
SRCREV_machine_qemux86-64 ?= "0cdc8564c61958a39704d97e008120bd7c762f60"
SRCREV_machine_qemumips64 ?= "766c469b1707fd3101116f572052a7b8d5ac0658"
SRCREV_machine ?= "0cdc8564c61958a39704d97e008120bd7c762f60"
SRCREV_meta ?= "607726b2d0b8e1d8c5653003448a24353a88c63b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.18;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.18.9"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

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
