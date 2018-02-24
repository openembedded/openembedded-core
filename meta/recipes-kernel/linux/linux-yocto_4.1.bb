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

SRCREV_machine_qemuarm ?= "8a40392c9aab6a3b25560c1a5d4a31d87a65a088"
SRCREV_machine_qemuarm64 ?= "a315195324973d3edbd4868a0d7acbd82b7d53ae"
SRCREV_machine_qemumips ?= "ea667187404992a98d49ea62313f826e6b7a9849"
SRCREV_machine_qemuppc ?= "8c3cd05eab5cf9c49847ed73d77f1d036808b5dc"
SRCREV_machine_qemux86 ?= "a315195324973d3edbd4868a0d7acbd82b7d53ae"
SRCREV_machine_qemux86-64 ?= "a315195324973d3edbd4868a0d7acbd82b7d53ae"
SRCREV_machine_qemumips64 ?= "4a026b26ffed34b40a7fb8fd190ee3eed964b5ea"
SRCREV_machine ?= "a315195324973d3edbd4868a0d7acbd82b7d53ae"
SRCREV_meta ?= "4e12cb8f8e06636f2058ea0ab3096ed38228a88b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.49"

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
