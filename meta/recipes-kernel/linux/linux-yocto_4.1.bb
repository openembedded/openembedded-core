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

SRCREV_machine_qemuarm ?= "cadb80aa4bcee282f1b0798ef35ad8b96ec44931"
SRCREV_machine_qemuarm64 ?= "403eda4633e9037fb715d0d1e8ae847b2bd0651a"
SRCREV_machine_qemumips ?= "737eda6388a529d0937e9d91daa3644b11f322dc"
SRCREV_machine_qemuppc ?= "403eda4633e9037fb715d0d1e8ae847b2bd0651a"
SRCREV_machine_qemux86 ?= "403eda4633e9037fb715d0d1e8ae847b2bd0651a"
SRCREV_machine_qemux86-64 ?= "403eda4633e9037fb715d0d1e8ae847b2bd0651a"
SRCREV_machine_qemumips64 ?= "f276d128428b031521d766bfbfe84f9f9f992930"
SRCREV_machine ?= "403eda4633e9037fb715d0d1e8ae847b2bd0651a"
SRCREV_meta ?= "d6e1f4bfc254c677a8dfef92f0ad8c78bdeeea75"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.24"

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
