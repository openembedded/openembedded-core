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

SRCREV_machine_qemuarm ?= "45b01a52f2a1593f7858cff130811c587d9623a9"
SRCREV_machine_qemuarm64 ?= "8149f5840e938ff5a0e595bac2f7502a2a9bcb40"
SRCREV_machine_qemumips ?= "46a71bdb936b8a5db5e85dfcc82fa6121d9aa2e5"
SRCREV_machine_qemuppc ?= "8149f5840e938ff5a0e595bac2f7502a2a9bcb40"
SRCREV_machine_qemux86 ?= "8149f5840e938ff5a0e595bac2f7502a2a9bcb40"
SRCREV_machine_qemux86-64 ?= "8149f5840e938ff5a0e595bac2f7502a2a9bcb40"
SRCREV_machine_qemumips64 ?= "9ee2a571138d4ca993ce03ce753c1cb776fdd441"
SRCREV_machine ?= "8149f5840e938ff5a0e595bac2f7502a2a9bcb40"
SRCREV_meta ?= "8900370d334ab4f7224fa71d7d46d62f0b11199d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.12"

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
