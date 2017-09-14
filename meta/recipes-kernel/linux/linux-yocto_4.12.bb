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

SRCREV_machine_qemuarm ?= "67b62d8d7bd9f19f0f4fad044787d947ebc4ec25"
SRCREV_machine_qemuarm64 ?= "16de0149674ed12d983b77a453852ac2e64584b4"
SRCREV_machine_qemumips ?= "371926551d44c140a0fb869cec34762b40bdf1a6"
SRCREV_machine_qemuppc ?= "16de0149674ed12d983b77a453852ac2e64584b4"
SRCREV_machine_qemux86 ?= "16de0149674ed12d983b77a453852ac2e64584b4"
SRCREV_machine_qemux86-64 ?= "16de0149674ed12d983b77a453852ac2e64584b4"
SRCREV_machine_qemumips64 ?= "9d63d9c11c44ceabd452d7ee49cf55a88c39383f"
SRCREV_machine ?= "16de0149674ed12d983b77a453852ac2e64584b4"
SRCREV_meta ?= "eda4d18ce4b259c84ccd5c249c3dc5958c16928c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.12"

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
