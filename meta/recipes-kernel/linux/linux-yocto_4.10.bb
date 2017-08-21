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

SRCREV_machine_qemuarm ?= "4c652d50c67050422da4621cc6421ecb86b31c31"
SRCREV_machine_qemuarm64 ?= "65370fa249e282e4ce69cf927c01898b4c16f261"
SRCREV_machine_qemumips ?= "0a632fa7f9615ad1b2488a300846a7e925e6591a"
SRCREV_machine_qemuppc ?= "65370fa249e282e4ce69cf927c01898b4c16f261"
SRCREV_machine_qemux86 ?= "65370fa249e282e4ce69cf927c01898b4c16f261"
SRCREV_machine_qemux86-64 ?= "65370fa249e282e4ce69cf927c01898b4c16f261"
SRCREV_machine_qemumips64 ?= "75c22c1be83bf7894af78a36b3e3c5af08ce4d7b"
SRCREV_machine ?= "65370fa249e282e4ce69cf927c01898b4c16f261"
SRCREV_meta ?= "6ac2680ca4316fe111cddec37def7757843bbe86"

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
