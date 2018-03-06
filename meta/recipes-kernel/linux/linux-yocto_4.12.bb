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

SRCREV_machine_qemuarm ?= "c9f0aa61969279f3ff36f2bc5dcad333f1e6fe59"
SRCREV_machine_qemuarm64 ?= "073873cb152cf254d229d548c3bdcc57460a4ff0"
SRCREV_machine_qemumips ?= "c327b2ea91c97957e865f90e7555aa4689cb5787"
SRCREV_machine_qemuppc ?= "073873cb152cf254d229d548c3bdcc57460a4ff0"
SRCREV_machine_qemux86 ?= "073873cb152cf254d229d548c3bdcc57460a4ff0"
SRCREV_machine_qemux86-64 ?= "073873cb152cf254d229d548c3bdcc57460a4ff0"
SRCREV_machine_qemumips64 ?= "d25e0aa3f09dff2b8b908c7764c3c5244134d1a4"
SRCREV_machine ?= "073873cb152cf254d229d548c3bdcc57460a4ff0"
SRCREV_meta ?= "6d6037cc054350140df49c309e155f5a7b48f480"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.20"

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
