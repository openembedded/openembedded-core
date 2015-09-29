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

SRCREV_machine_qemuarm ?= "bbd539ee6f9c8e7871e9c55607dc15807e4da492"
SRCREV_machine_qemuarm64 ?= "cf940efcae03adea9f17be3a21406f7a2a385b72"
SRCREV_machine_qemumips ?= "8027a8514b7c56334ab2414b36f3eed751f1977d"
SRCREV_machine_qemuppc ?= "cf940efcae03adea9f17be3a21406f7a2a385b72"
SRCREV_machine_qemux86 ?= "cf940efcae03adea9f17be3a21406f7a2a385b72"
SRCREV_machine_qemux86-64 ?= "cf940efcae03adea9f17be3a21406f7a2a385b72"
SRCREV_machine_qemumips64 ?= "808d3efb9d8f94c0c5d3d316938679a7583f52ef"
SRCREV_machine ?= "cf940efcae03adea9f17be3a21406f7a2a385b72"
SRCREV_meta ?= "3d8f1378d07dbc052ca8a7c22297339ad7998b5e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.8"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
