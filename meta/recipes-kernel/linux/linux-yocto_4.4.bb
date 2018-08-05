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

SRCREV_machine_qemuarm ?= "acf04f09f4f2d2b568618b20171cf45afd6b2e28"
SRCREV_machine_qemuarm64 ?= "7c4dd5edc287abd270a97eb38cee98f0d0318418"
SRCREV_machine_qemumips ?= "df50bef6ecd2df365c203cc400920f6560e26a8c"
SRCREV_machine_qemuppc ?= "7c4dd5edc287abd270a97eb38cee98f0d0318418"
SRCREV_machine_qemux86 ?= "7c4dd5edc287abd270a97eb38cee98f0d0318418"
SRCREV_machine_qemux86-64 ?= "7c4dd5edc287abd270a97eb38cee98f0d0318418"
SRCREV_machine_qemumips64 ?= "27acba48690468efe0a888fcd68493c82658c7c2"
SRCREV_machine ?= "7c4dd5edc287abd270a97eb38cee98f0d0318418"
SRCREV_meta ?= "b41a36ffe53f73c86a0f3672d32b5ebec59ab15e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.141"

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
