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

SRCREV_machine_qemuarm ?= "0151a12b692fe02660a7bcf1edda81b2468c016b"
SRCREV_machine_qemuarm64 ?= "ae89dd60fe3a5fd9a7c2d8e1859e0c860333b863"
SRCREV_machine_qemumips ?= "ea167e2fdb145ef2bccd79234ac43ac3fee0fb67"
SRCREV_machine_qemuppc ?= "ae89dd60fe3a5fd9a7c2d8e1859e0c860333b863"
SRCREV_machine_qemux86 ?= "ae89dd60fe3a5fd9a7c2d8e1859e0c860333b863"
SRCREV_machine_qemux86-64 ?= "ae89dd60fe3a5fd9a7c2d8e1859e0c860333b863"
SRCREV_machine_qemumips64 ?= "5b8d8e6d06206f59662075f74126352a500df106"
SRCREV_machine ?= "ae89dd60fe3a5fd9a7c2d8e1859e0c860333b863"
SRCREV_meta ?= "a4f88c3fad887e1c559d03ae1b531ca267137b69"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
