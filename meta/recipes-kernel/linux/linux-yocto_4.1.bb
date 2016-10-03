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

SRCREV_machine_qemuarm ?= "a8169bc7216752813a0a8be7d77ec8fcf0d8850e"
SRCREV_machine_qemuarm64 ?= "0c7be943fb59eb9a8d1e57e1fb1aa20aed5fa9cb"
SRCREV_machine_qemumips ?= "d12861e340503a14614b655e77d8dcabadb18f8e"
SRCREV_machine_qemuppc ?= "0e7c356e7c972e0f6f596d7d5f2621a3f68df8e4"
SRCREV_machine_qemux86 ?= "0c7be943fb59eb9a8d1e57e1fb1aa20aed5fa9cb"
SRCREV_machine_qemux86-64 ?= "0c7be943fb59eb9a8d1e57e1fb1aa20aed5fa9cb"
SRCREV_machine_qemumips64 ?= "c14be1d25b733ff6ff47e8f255e8196cf9b3e8c9"
SRCREV_machine ?= "0c7be943fb59eb9a8d1e57e1fb1aa20aed5fa9cb"
SRCREV_meta ?= "322fa5b2796bfcff7bfbbde1d76c73636ecf5857"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.33"

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
