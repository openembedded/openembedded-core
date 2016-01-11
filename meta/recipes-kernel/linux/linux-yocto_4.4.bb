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

SRCREV_machine_qemuarm ?= "bcfc111e6c14b9f9d6ffa9d357651c0b4489835e"
SRCREV_machine_qemuarm64 ?= "2dadc3524fcbce0c46f5db65b7c20c673fc60503"
SRCREV_machine_qemumips ?= "9d9c37432e75eaeb2232e00cc3c2252440b709b3"
SRCREV_machine_qemuppc ?= "2dadc3524fcbce0c46f5db65b7c20c673fc60503"
SRCREV_machine_qemux86 ?= "2dadc3524fcbce0c46f5db65b7c20c673fc60503"
SRCREV_machine_qemux86-64 ?= "2dadc3524fcbce0c46f5db65b7c20c673fc60503"
SRCREV_machine_qemumips64 ?= "cb3a85329501f541ebfa08febb2f36edcbc6f253"
SRCREV_machine ?= "2dadc3524fcbce0c46f5db65b7c20c673fc60503"
SRCREV_meta ?= "ad9d3f01300ba350563e17db00b2518302e172f6"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4"

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
