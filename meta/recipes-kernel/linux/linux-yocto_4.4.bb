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

SRCREV_machine_qemuarm ?= "c2507bd62854f90030433307a3d783474332f821"
SRCREV_machine_qemuarm64 ?= "ddab242999407fadae68e7ee5381b0ec6679d443"
SRCREV_machine_qemumips ?= "e42198f28a2c3e57ef64a0f5ccfdeed906d1c257"
SRCREV_machine_qemuppc ?= "ddab242999407fadae68e7ee5381b0ec6679d443"
SRCREV_machine_qemux86 ?= "ddab242999407fadae68e7ee5381b0ec6679d443"
SRCREV_machine_qemux86-64 ?= "ddab242999407fadae68e7ee5381b0ec6679d443"
SRCREV_machine_qemumips64 ?= "2d0080f2350e088bdc6129c376eb9e2654a0448a"
SRCREV_machine ?= "ddab242999407fadae68e7ee5381b0ec6679d443"
SRCREV_meta ?= "86bf91f444418076181fedba14bdafad3a531cf0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.15"

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
